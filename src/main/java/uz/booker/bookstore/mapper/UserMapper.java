package uz.booker.bookstore.mapper;

import org.mapstruct.Mapper;
import uz.booker.bookstore.dto.UserDto;
import uz.booker.bookstore.entity.user.User;

@Mapper(componentModel = "spring")
public interface UserMapper extends BaseMapper<User, UserDto> {




}
