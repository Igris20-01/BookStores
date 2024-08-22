package uz.booker.bookstore.service.impl;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import uz.booker.bookstore.dto.ChangePasswordRequest;
import uz.booker.bookstore.dto.UserUpdateDto;
import uz.booker.bookstore.entity.user.User;
import uz.booker.bookstore.entity.user.UserImage;
import uz.booker.bookstore.repository.jpa.UserImageRepo;
import uz.booker.bookstore.repository.jpa.UserRepository;
import uz.booker.bookstore.service.interfaces.UserService;
import uz.booker.bookstore.util.ImageUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class UserServiceI implements UserDetailsService, UserService {

    // TODO work with this @Transactional(propagation = Propagation.NOT_SUPPORTED)
    //  and work with Thread.sleep();
    //  NOT_SUPPORTED, NEVER, REQUIRES_NEW

    @Value("${photo.profile.storage.path}")
    private String photoAvatarStoragePath;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final JwtServiceI jwtService;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private UserImageRepo userImageRepo;

    private static final Logger log = LoggerFactory.getLogger(UserServiceI.class);


    @Override
    public void updateUser(Long id, UserUpdateDto updatedUserDto) {
        userRepository.findById(id)
                .map(existingUser -> {
                    log.info("Updating user with id {}: {}", id, updatedUserDto);
                    String oldEmail = existingUser.getEmail();
                    existingUser.setFullName(updatedUserDto.getFullName());
                    User updatedUser = userRepository.save(existingUser);
                    log.info("User updated successfully: {}", updatedUser);

                    if (!oldEmail.equals(updatedUser.getEmail())) {
                        log.info("User email has been updated. Invalidating current token.");
                        jwtService.invalidateToken(oldEmail);
                    }
                    return updatedUser;
                })
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public void changePasswordById(Long userId, ChangePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found"));
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Passwords do not match");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Trying to load user by username: {}", username);
        return userRepository.findByEmail(username)
                .orElseThrow(() -> {
                    log.error("User with email {} not found", username);
                    return new UsernameNotFoundException("User not found " + username);});

    }

    @Override
    public void uploadProfileImage(Long userId, MultipartFile file)  {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        UserImage profileImage = saveImage(userId, file); // Pass userId here
        profileImage.setUser(user);
        userImageRepo.save(profileImage);
    }

    @Override
    public UserImage saveImage(Long userId, MultipartFile file)  {
        try {
            String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
            String generatedFileName = UUID.randomUUID().toString() + "_" + originalFileName;
            String destinationImagePath = photoAvatarStoragePath;
            byte[] resizedImage = ImageUtil.resizeImage(file.getBytes(), 2000, 5000);
            Path destinationPath = Paths.get(destinationImagePath, generatedFileName);
            Files.write(destinationPath, resizedImage);
            UserImage userImage = new UserImage();
            userImage.setOriginalName(originalFileName);
            userImage.setPathWithOriginalName(destinationImagePath);
            userImage.setGenerateName(generatedFileName);
            userImage.setUserId(userId);
            return userImage;
        }
        catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public byte[] getProfileImage(Long userId) throws IOException {
        UserImage userImage = userImageRepo.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Profile image not found for user"));
        String imagePath = userImage.getPathWithOriginalName();
        String imageName = userImage.getGenerateName();
        Path imageFilePath = Paths.get(imagePath, imageName);
        return Files.readAllBytes(imageFilePath);
    }

}
