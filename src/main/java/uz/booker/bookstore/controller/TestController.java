package uz.booker.bookstore.controller;

import lombok.RequiredArgsConstructor;
import jakarta.ws.rs.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TestController {

    @GetMapping("/test")
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Here is your resource");
    }

    @GetMapping("/throw/{id}")
    public ResponseEntity<Void> thr (@PathVariable Long id) throws BadRequestException {

        if (id == 2) {
            System.out.println("error keldi");
            throw new BadRequestException("keldi");
        }

        return ResponseEntity.ok().build();
    }


}
