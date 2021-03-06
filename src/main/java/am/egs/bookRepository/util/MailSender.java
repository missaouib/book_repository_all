package am.egs.bookRepository.util;

import am.egs.bookRepository.model.User;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Data
public class MailSender {
private final JavaMailSender mailSender;

    @Value("${server.port}")
    private int port;

    @Value(value = "${spring.mail.username}")
    private String userName;

    public MailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void send(User user) {
        String subject = "Activation Code";
        String message = String.format(
                "Hello, %s! welcome " +
                        "click on this link to activate your email " +
                        "http://localhost:%s/main/activate/%s",
                user.getName(), port, user.getActivationCode()
        );

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(userName);
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        mailSender.send(mailMessage);
    }
}
