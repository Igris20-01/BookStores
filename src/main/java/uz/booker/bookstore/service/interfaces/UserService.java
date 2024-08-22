package uz.booker.bookstore.service.interfaces;


import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;
import uz.booker.bookstore.dto.UserUpdateDto;
import uz.booker.bookstore.entity.user.User;
import uz.booker.bookstore.entity.user.UserImage;

import java.io.IOException;

public interface UserService {

    void updateUser(Long id, UserUpdateDto updatedUserDto);

    @Transactional
    void uploadProfileImage(Long userId, MultipartFile file);

    @Transactional
    UserImage saveImage(Long userId, MultipartFile file);

    @Transactional
    byte[] getProfileImage(Long userId) throws IOException;


}
