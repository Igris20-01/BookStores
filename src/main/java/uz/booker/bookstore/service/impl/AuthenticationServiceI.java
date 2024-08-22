package uz.booker.bookstore.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.booker.bookstore.dto.JwtAuthenticationResponse;
import uz.booker.bookstore.dto.JwtAuthorizationResponse;
import uz.booker.bookstore.dto.SignInRequest;
import uz.booker.bookstore.dto.SignUpRequest;
import uz.booker.bookstore.entity.other.Otp;
import uz.booker.bookstore.entity.token.Token;
import uz.booker.bookstore.entity.user.User;
import uz.booker.bookstore.enums.TokenType;
import uz.booker.bookstore.repository.jpa.OtpRepository;
import uz.booker.bookstore.repository.jpa.TokenRepository;
import uz.booker.bookstore.repository.jpa.UserRepository;
import uz.booker.bookstore.service.interfaces.AccountService;
import uz.booker.bookstore.service.interfaces.AuthenticationService;
import uz.booker.bookstore.service.interfaces.JwtService;
import uz.booker.bookstore.util.EmailUtil;
import uz.booker.bookstore.util.OtpUtil;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import static uz.booker.bookstore.enums.Role.ADMIN;
import static uz.booker.bookstore.enums.Role.USER;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceI implements AuthenticationService {

    UserRepository userRepository;

    TokenRepository tokenRepository;

    PasswordEncoder passwordEncoder;

    JwtService jwtService;

    AuthenticationManager authenticationManager;

    OtpRepository otpRepository;

    OtpUtil otpUtil;

    EmailUtil emailUtil;

    AccountService accountService;

    static Logger logger = LoggerFactory.getLogger(AuthenticationService.class);


    @Override
    public JwtAuthorizationResponse signUp(SignUpRequest request)  {

        var user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(USER)
                .build();
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Password and confirmation password do not match");
        }
        userRepository.save(user);
        String otp = otpUtil.generateOtp();
        emailUtil.sendOtpEmail(user.getEmail(), otp);
        Otp otpCode = Otp.builder()
                .userId(user.getId())
                .otp(otp)
                .otpGeneratedTime(LocalDateTime.now())
                .build();
        otpRepository.save(otpCode);
        return JwtAuthorizationResponse.builder().status("CREATED").build();
    }

    @Override
    public JwtAuthorizationResponse registerAdmin(SignUpRequest request){
        var admin = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(ADMIN)
                .build();
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Password and confirmation password do not match");
        }
        userRepository.save(admin);
        return JwtAuthorizationResponse.builder().status("CREATED").build();
    }

    @Override
    public String verifyAccount(String email, String otp) {
        Otp otpEntity = otpRepository.findByUserIdAndOtp(
                userRepository.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("User not found with this email: " + email)).getId(),
                otp
        ).orElseThrow(() -> new RuntimeException("Invalid OTP"));
        if (Duration.between(otpEntity.getOtpGeneratedTime(), LocalDateTime.now()).getSeconds() < (60)) {
            User user = otpEntity.getUser();
            user.setActive(true);
            userRepository.save(user);
            return "OTP verified you can login";
        }
        return "Please regenerate OTP and try again";
    }

    @Override
    public String regenerateOtp(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
        String otp = otpUtil.generateOtp();
        emailUtil.sendOtpEmail(email, otp);
        Optional<Otp> existingOtp = otpRepository.findByUserId(user.getId());
        if (existingOtp.isPresent()) {
            Otp otpEntity = existingOtp.get();
            otpEntity.setOtp(otp);
            otpEntity.setOtpGeneratedTime(LocalDateTime.now());
            otpRepository.save(otpEntity);
        } else {
            var otpEntity = Otp.builder()
                    .otp(otp)
                    .otpGeneratedTime(LocalDateTime.now())
                    .userId(user.getId())
                    .user(user)
                    .build();
            otpRepository.save(otpEntity);
        }
        return "Email sent... please verify account within 1 minute";
    }





    @Override
    public JwtAuthenticationResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        if (user.isDeleted()) {
            accountService.restoreAccount(user.getEmail());
        }
        // Check for inactive accounts and incorrect passwords
        if (!user.isActive()) {
            throw new IllegalStateException("Your account is not verified");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Password is incorrect");
        }


        var jwt = jwtService.generateToken(user);
        var token = Token.builder()
                .user(user)
                .token(jwt)
                .tokenType(TokenType.BEARER)
                .revoke(false)
                .expired(false)
                .build();
        revokeAllUserTokens(user);
        tokenRepository.save(token);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

    @Override
    public String forgotPassword(String email) {
        userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new RuntimeException("User not found with this email: " + email));
        emailUtil.sendSetPasswordEmail(email);
        return "Please check your email to set new password to your account";
    }

    @Override
    public String setPassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new RuntimeException("User not found with this email: " + email));
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        userRepository.save(user);
        return "New password set successfully login with new password";
    }

    @Override
    public void revokeAllUserTokens(User user){
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(t -> {
            t.setExpired(true);
            t.setRevoke(true);
        });
        tokenRepository.saveAll(validUserTokens);

    }




}
