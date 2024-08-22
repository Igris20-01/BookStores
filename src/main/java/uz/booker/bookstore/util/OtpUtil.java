package uz.booker.bookstore.util;


import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Component
public class OtpUtil {

    public String generateOtp(){
        return ThreadLocalRandom.current()
                .ints(100_000, 1_000_000)
                .distinct()
                .limit(1)
                .mapToObj(Integer::toString)
                .collect(Collectors.joining());

    }
}
