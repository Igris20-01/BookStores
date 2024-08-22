package uz.booker.bookstore.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.booker.bookstore.entity.BaseEntity;
import uz.booker.bookstore.enums.Role;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;


@Data
@Entity
@Table(name = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User extends BaseEntity implements UserDetails, Serializable {


    @Column(name = "full_name",nullable = false)
    String fullName;

    @Column(name = "email",unique = true, nullable = false)
    String email;

    @Column(name = "password",nullable = false)
    @JsonIgnore
    String password;

    String gender;

    LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    Role role;

    @Column(name = "delete",columnDefinition = "boolean default false" )
    boolean deleted = false;

    LocalDate deletedAt;

    LocalDate restoreAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
