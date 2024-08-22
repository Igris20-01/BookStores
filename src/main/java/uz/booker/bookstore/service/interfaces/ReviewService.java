package uz.booker.bookstore.service.interfaces;

import uz.booker.bookstore.dto.ReplyCreateDto;
import uz.booker.bookstore.dto.ReviewCreateDto;
import uz.booker.bookstore.dto.ReviewDto;
import uz.booker.bookstore.entity.other.Review;

import java.util.UUID;

public interface ReviewService {
    ReviewDto createReviewForBook(ReviewCreateDto reviewCreateDto);

    ReviewDto createReplyToReview(ReplyCreateDto replyCreateDto);

    ReviewDto getReviewById(UUID reviewId);

    void deleteReviewById(UUID reviewId);

    ReviewDto mapReviewToDto(Review review);
}
