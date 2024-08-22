package uz.booker.bookstore.service.impl;


import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import uz.booker.bookstore.entity.user.User;
import uz.booker.bookstore.repository.jpa.UserRepository;
import uz.booker.bookstore.service.interfaces.AccountService;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountServiceI implements AccountService {

    @Autowired
    UserRepository userRepository;


    @Override
    @Transactional
    public void deleteAccount(String email) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return;
        }
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        if (!principal.getUsername().equals(email)) {
            throw new AccessDeniedException("Forbidden");
        }
        if(!isCurrentUserOwnerOfAccount(email)){
            throw new AccessDeniedException("Forbidden");
        }
        userRepository.softDeleteByEmail(email);
    }

    private boolean isCurrentUserOwnerOfAccount(String email){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        return principal.getUsername().equals(email);
    }


    @Override
    public void restoreAccount(String email) {
        User account = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));
        account.setActive(true);
        account.setDeleted(false);
        account.setDeletedAt(null);
        account.setRestoreAt(LocalDate.now());
        userRepository.save(account);
    }

    @Override
    @Scheduled(cron = "0 0 * * * *") // Ежедневно в полночь в 00:00:00
    public void cleanupDeletedAccounts() {
        LocalDate threshold = LocalDate.now().minusDays(30);
        List<User> deletedUsers = userRepository.findByDeletedTrueAndDeletedAtBefore(threshold);
        for (User user : deletedUsers) {
            userRepository.deleteById(user.getId());
        }
    }
}
