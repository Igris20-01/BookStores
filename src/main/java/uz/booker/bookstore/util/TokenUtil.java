package uz.booker.bookstore.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import uz.booker.bookstore.repository.jpa.TokenRepository;

@Component
public class TokenUtil {

    @Autowired
    private TokenRepository tokenRepository;


    @Scheduled(fixedRate = 60000)
    public void cleanupExpiredAndRevokedTokens() {
        tokenRepository.deleteByRevokeAndExpired(true,true);
    }


}
