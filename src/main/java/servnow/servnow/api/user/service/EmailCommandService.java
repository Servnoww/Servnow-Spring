package servnow.servnow.api.user.service;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import servnow.servnow.common.code.UserErrorCode;
import servnow.servnow.common.exception.NotFoundException;

@Service
@RequiredArgsConstructor
public class EmailCommandService {

    private final JavaMailSender emailSender;
    private final EmailVerificationService emailVerificationService;
    private final EmailCodeGenerator emailCodeGenerator;

    public void sendVerificationEmail(String email) throws Exception {
        String code = emailCodeGenerator.generateCode();

        // Redis에 인증번호 저장
        emailVerificationService.saveVerificationCode(email, code);

        // 이메일 발송
        MimeMessage message = createMessage(email, code);
        try {
            emailSender.send(message);
        } catch (MailException e) {
            throw new NotFoundException(UserErrorCode.SEND_CERTIFICATION_NUMBER);
        }
    }

    private MimeMessage createMessage(String to, String code) throws Exception {
        MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, to);
        message.setSubject("이메일 인증 코드");

        String msgg = getMsgg(code);
        message.setText(msgg, "utf-8", "html");
        message.setFrom(new InternetAddress("SERVNOW@gmail.com", "SERVNOW"));

        return message;
    }

    private String getMsgg(String code) {
        return "<div style='margin:15px;'>"
                + "<h1>SERVNOW</h1>"
                + "<p>아래 코드를 복사해 입력해주세요</p>"
                + "<div style='border:1px solid black; text-align:center;'>"
                + "<h3>이메일 인증 코드</h3>"
                + "<p><strong>" + code + "</strong></p>"
                + "</div></div>";
    }
}
