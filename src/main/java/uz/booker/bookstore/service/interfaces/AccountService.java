package uz.booker.bookstore.service.interfaces;

import org.springframework.scheduling.annotation.Scheduled;

public interface AccountService {

    void deleteAccount(String email);

    void restoreAccount(String email);

    @Scheduled(cron = "0 0 * * * *") // Ежедневно в полночь
    void cleanupDeletedAccounts();



}
