package uz.booker.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.booker.bookstore.dto.ReplyCreateDto;
import uz.booker.bookstore.dto.ReviewCreateDto;
import uz.booker.bookstore.dto.ReviewDto;
import uz.booker.bookstore.service.interfaces.ReviewService;

import java.util.UUID;

@RestController
@RequestMapping("/review")
@Tag(name = "Review", description = "Review management APIs")
public class  ReviewController {

    @Autowired
    private ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Operation(summary = "create-review", description = "bar")
    @PostMapping("/create")
    public ResponseEntity<ReviewDto> createReviewForBook(@RequestBody ReviewCreateDto reviewCreateDto) {
        ReviewDto createdReview = reviewService.createReviewForBook(reviewCreateDto);
        return ResponseEntity.ok(createdReview);
    }

    @Operation(summary = "replay-review", description = "bar")
    @PostMapping("/{reviewId}/reply")
    public ResponseEntity<ReviewDto> createReplyToReview(@PathVariable UUID reviewId, @RequestBody ReplyCreateDto replyCreateDto) {
        replyCreateDto.setReviewId(reviewId);
        ReviewDto createdReply = reviewService.createReplyToReview(replyCreateDto);
        return ResponseEntity.ok(createdReply);
    }

    @Operation(summary = "get-review", description = "bar")
    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewDto> getReview(@PathVariable UUID reviewId) {
        ReviewDto review = reviewService.getReviewById(reviewId);
        return ResponseEntity.ok(review);
    }

    @Operation(summary = "delete-review", description = "bar")
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable UUID reviewId) {
        reviewService.deleteReviewById(reviewId);
        return ResponseEntity.noContent().build();
    }
}
