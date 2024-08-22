package uz.booker.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.booker.bookstore.dto.FeedBackDto;
import uz.booker.bookstore.service.impl.FeedBackServiceI;

import java.util.UUID;

@RestController
@RequestMapping("/feedback")
@Tag(name = "FeedBack", description = "FeedBack management APIs")
public class FeedBackController {

    private final FeedBackServiceI feedbackService;

    public FeedBackController(FeedBackServiceI feedbackService) {
        this.feedbackService = feedbackService;
    }

    @Operation(summary = "send-feedback", description = "bar")
    @PostMapping("/send")
    public ResponseEntity<String> sendFeedback(@RequestBody FeedBackDto feedBackDto) {
        feedbackService.sendFeedback(feedBackDto);
        return ResponseEntity.ok("Feedback sent successfully.");
    }

    @Operation(summary = "get-feedback", description = "bar")
    @GetMapping("/{id}")
    public ResponseEntity<FeedBackDto> getFeedbackById(@PathVariable UUID id) {
        FeedBackDto feedback = feedbackService.getFeedbackById(id);
        return ResponseEntity.ok(feedback);
    }


}
