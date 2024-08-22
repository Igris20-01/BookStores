package uz.booker.bookstore.controller;



import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.booker.bookstore.dto.ChangePasswordRequest;
import uz.booker.bookstore.dto.UserUpdateDto;
import uz.booker.bookstore.service.impl.UserServiceI;
import uz.booker.bookstore.service.interfaces.AccountService;

import java.io.IOException;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "User", description = "User management APIs")
public class UserController {

    @Autowired
    UserServiceI service;

    AccountService accountService;

    @Operation(summary = "edit-profile", description = "bar")
    @PutMapping("/edit-profile/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserUpdateDto updatedUserDto) {
        service.updateUser(id, updatedUserDto);
        return ResponseEntity.ok("User updated successfully");
    }

    @Operation(summary = "change-password", description = "bar")
    @PutMapping("/{id}/change-password")
    public ResponseEntity<?> changePasswordById(@PathVariable Long id, @RequestBody ChangePasswordRequest passwordRequest) {
        service.changePasswordById(id, passwordRequest);
        return ResponseEntity.ok("Password changed successfully");
    }

    @Operation(summary = "delete-account", description = "bar")
    @DeleteMapping("/delete-account/{email}")
    public ResponseEntity<Void> deleteUser(@PathVariable String email){
        accountService.deleteAccount(email);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "add-profile-image", description = "bar")
    @PostMapping("/{userId}/profile-image")
    public ResponseEntity<String> uploadProfileImage(@PathVariable Long userId, @RequestParam("file") MultipartFile file) {
        service.uploadProfileImage(userId, file);
        return ResponseEntity.ok("Profile image uploaded successfully");

    }

    @Operation(summary = "get-profile-image", description = "bar")
    @GetMapping(value = "/{userId}/profile-images", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getUserProfileImage(@PathVariable Long userId) {
        try {
            byte[] imageBytes = service.getProfileImage(userId);
            return ResponseEntity.ok().body(imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
