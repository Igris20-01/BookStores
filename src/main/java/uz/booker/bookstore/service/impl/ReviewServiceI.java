package uz.booker.bookstore.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.booker.bookstore.dto.ReplyCreateDto;
import uz.booker.bookstore.dto.ReviewCreateDto;
import uz.booker.bookstore.dto.ReviewDto;
import uz.booker.bookstore.entity.other.Review;
import uz.booker.bookstore.repository.jpa.ReviewRepository;
import uz.booker.bookstore.service.interfaces.ReviewService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewServiceI implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public ReviewDto createReviewForBook(ReviewCreateDto reviewCreateDto){
        Review review = new Review();
        review.setBookId(reviewCreateDto.getBookId());
        review.setUserId(reviewCreateDto.getUserId());
        review.setComment(reviewCreateDto.getComment());
        review.setCreatedAt(new Date());

        Review saved = reviewRepository.save(review);

        return mapReviewToDto(saved);
    }
    @Override
    public ReviewDto createReplyToReview(ReplyCreateDto replyCreateDto){
        Review parentReview = reviewRepository.findById(replyCreateDto.getReviewId())
                .orElseThrow(() -> new RuntimeException("Review not found"));
        Review reply = new Review();
        reply.setParentId(parentReview.getId());
        reply.setBookId(parentReview.getBookId());
        reply.setUserId(replyCreateDto.getUserId());
        reply.setComment(replyCreateDto.getComment());
        reply.setCreatedAt(new Date());
        Review savedReply = reviewRepository.save(reply);
        parentReview.getReplies().add(savedReply);
        reviewRepository.save(parentReview);
        return mapReviewToDto(savedReply);
    }

    @Override
    public ReviewDto getReviewById(UUID reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        return mapReviewToDto(review);
    }

    @Override
    public void deleteReviewById(UUID reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        reviewRepository.delete(review);
    }

    @Override
    public ReviewDto mapReviewToDto(Review review){
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setId(review.getId());
        reviewDto.setComment(review.getComment());
        reviewDto.setBookId(review.getBookId());
        reviewDto.setUserId(review.getUserId());
        reviewDto.setCreatedAt(review.getCreatedAt());

        List<ReviewDto> replyDto = new ArrayList<>();
        for(Review reply : review.getReplies()){
            replyDto.add(mapReviewToDto(reply));
        }

        reviewDto.setReplies(replyDto);
        return reviewDto;
    }

}

