package uz.booker.bookstore.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailUtil {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        javaMailSender.send(message);
    }


    public void  sendOtpEmail(String email, String otp){
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject("Verify OTP");
            mimeMessageHelper.setText("""
                    <div>
                             <a href="http://localhost:8080/api/v1/verify-account?email=%s&otp=%s" target="_blank">click link to verify</a>
                           </div>""".formatted(email, otp), true);


            javaMailSender.send(mimeMessage);
        }catch (MessagingException e){
            e.printStackTrace();

        }

    }

    public void  sendSetPasswordEmail(String email){
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject("Send Password");
            mimeMessageHelper.setText("""
                    <div>
                             <a href="http://localhost:8080/api/v1/set-password?email=%s" target="_blank">click link to set password</a>
                           </div>""".formatted(email), true);


            javaMailSender.send(mimeMessage);
        }catch (MessagingException e){
            e.printStackTrace();

        }

    }

}
