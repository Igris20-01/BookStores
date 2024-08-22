package uz.booker.bookstore.service.interfaces;

import uz.booker.bookstore.dto.JwtAuthenticationResponse;
import uz.booker.bookstore.dto.JwtAuthorizationResponse;
import uz.booker.bookstore.dto.SignInRequest;
import uz.booker.bookstore.dto.SignUpRequest;
import uz.booker.bookstore.entity.user.User;

public interface AuthenticationService {
    JwtAuthorizationResponse signUp(SignUpRequest request);

    JwtAuthorizationResponse registerAdmin(SignUpRequest request);

    String verifyAccount(String email, String otp);

    String regenerateOtp(String email);

    JwtAuthenticationResponse signIn(SignInRequest request);

    String forgotPassword(String email);

    String setPassword(String email, String newPassword);

    void revokeAllUserTokens(User user);
}
