package uz.booker.bookstore.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.booker.bookstore.dto.JwtAuthenticationResponse;
import uz.booker.bookstore.dto.JwtAuthorizationResponse;
import uz.booker.bookstore.dto.SignInRequest;
import uz.booker.bookstore.dto.SignUpRequest;
import uz.booker.bookstore.service.interfaces.AuthenticationService;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthController {

    private final AuthenticationService authenticationService;

    @Operation(summary = "register", description = "bar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation")
    })
    @PostMapping("/register")
    public ResponseEntity<JwtAuthorizationResponse> signUp(@RequestBody SignUpRequest request) {
        return ResponseEntity.ok(authenticationService.signUp(request));
    }

    @Operation(summary = "verification", description = "bar")
    @PutMapping("/verify-account")
    public ResponseEntity<?> verifyAccount(@RequestParam String email,@RequestParam String otp){
        return new ResponseEntity<>(authenticationService.verifyAccount(email, otp), HttpStatus.OK);
    }

    @Operation(summary = "regenerate-otp", description = "bar")
    @PutMapping("/regenerate-otp")
    public ResponseEntity<?> regenerateOtp(@RequestParam String email){
        return new ResponseEntity<>(authenticationService.regenerateOtp(email), HttpStatus.OK);
    }

    @Operation(summary = "register-admin", description = "bar")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/register-Admin")
    public ResponseEntity<JwtAuthorizationResponse> registerAdmin(
            @RequestBody SignUpRequest request
    ){
        return ResponseEntity.ok(authenticationService.registerAdmin(request));
    }

    @Operation(summary = "login", description = "bar")
    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> signIn(@RequestBody SignInRequest request) {
        return ResponseEntity.ok(authenticationService.signIn(request));
    }

    @Operation(summary = "forgot-pwd", description = "bar")
    @PutMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email){
        return  new ResponseEntity<>(authenticationService.forgotPassword(email),HttpStatus.OK);
    }

    @Operation(summary = "change-password", description = "bar")
    @PutMapping("/set-password")
    public ResponseEntity<?> setPassword(@RequestParam String email,@RequestHeader String newPassword){
        return new ResponseEntity<>(authenticationService.setPassword(email,newPassword),HttpStatus.OK);
    }

}