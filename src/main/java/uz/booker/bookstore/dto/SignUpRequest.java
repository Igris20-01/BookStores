package uz.booker.bookstore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SignUpRequest {

    @JsonProperty("name")
    String fullName;

    String email;

    String password;

    @JsonProperty("confirmation")
    String confirmPassword;




}
