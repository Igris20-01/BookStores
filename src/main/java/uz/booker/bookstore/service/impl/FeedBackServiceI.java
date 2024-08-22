package uz.booker.bookstore.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import uz.booker.bookstore.dto.FeedBackDto;
import uz.booker.bookstore.entity.other.FeedBack;
import uz.booker.bookstore.repository.jpa.FeedBackRepository;
import uz.booker.bookstore.util.EmailUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FeedBackServiceI {

    @Autowired
    private EmailUtil emailUtil;

    @Autowired
    private FeedBackRepository feedBackRepo;

    @Value("${admin.feedback.email}")
    private String adminEmail;
    public void sendFeedback(FeedBackDto feedBackDto) {
        FeedBack feedback = new FeedBack();
        feedback.setMessage(feedBackDto.getMessage());
        feedback.setSenderName(feedBackDto.getSenderName());
        feedback.setSenderEmail(feedBackDto.getSenderEmail());
        feedback.setCreateAt(LocalDateTime.now());
        feedBackRepo.save(feedback);
        String subject = "Новая обратная связь от " + feedBackDto.getSenderName();
        String body = "Сообщение: " + feedBackDto.getMessage() + "\nОтправитель: " + feedBackDto.getSenderName() + " (" + feedBackDto.getSenderEmail() + ")";
        emailUtil.sendEmail(adminEmail, subject, body);

    }

    public FeedBackDto getFeedbackById(UUID id){
        FeedBack feedback = feedBackRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Feedback not found"));
        return convertToDto(feedback);
    }


    @Scheduled(fixedRate = 31536000000L)
    public void deleteFeedbackOlderThanOneYear() {
        LocalDateTime oneYearAgo = LocalDateTime.now().minusYears(1);
        List<FeedBack> oldFeedbacks = feedBackRepo.findByCreateAtBefore(oneYearAgo);
        feedBackRepo.deleteAll(oldFeedbacks);
    }


    private FeedBackDto convertToDto(FeedBack feedback) {
        FeedBackDto dto = new FeedBackDto();
        dto.setId(feedback.getId());
        dto.setMessage(feedback.getMessage());
        dto.setSenderName(feedback.getSenderName());
        dto.setSenderEmail(feedback.getSenderEmail());
        return dto;
    }
}
