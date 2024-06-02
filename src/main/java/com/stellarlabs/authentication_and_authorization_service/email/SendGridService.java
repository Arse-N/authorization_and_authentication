package com.stellarlabs.authentication_and_authorization_service.email;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import com.stellarlabs.authentication_and_authorization_service.dto.notification.NotificationResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SendGridService {


    @Value("${spring.sendgrid.api-key}")
    private String sendGridApiKey;

    @Value("${email-verification.teamplateID}")
    private String emailVerificationID;

    @Value("${forgot-password.teamplateID}")
    private String forgotPasswordID;

    public void sendEmail(String recipientEmail, String subject, NotificationResponseDto notificationResponseDto) throws Exception {
        Email from = new Email("arsen.karapetyan33@gmail.com");
        Email to = new Email(recipientEmail);
        Mail mail = new Mail();
        mail.setFrom(from);
        Personalization personalization = new Personalization();
        switch (notificationResponseDto.getCode()){
            case 1002:
                personalization.addDynamicTemplateData("name", notificationResponseDto.getFirstName());
                personalization.addDynamicTemplateData("verification_url", notificationResponseDto.getLink());
                mail.setTemplateId(emailVerificationID);
                break;
            case 1001:
                personalization.addDynamicTemplateData("name", notificationResponseDto.getFirstName());
                personalization.addDynamicTemplateData("reset_password_url", notificationResponseDto.getLink());
                mail.setTemplateId(forgotPasswordID);
                break;
        }
        personalization.addTo(to);
        mail.addPersonalization(personalization);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println("Status ===>" + response.getStatusCode());
            System.out.println("Header ===>" + response.getHeaders());
            System.out.println("Body ===>" + response.getBody());
        } catch (IOException ex) {
            throw new Exception("Error sending email", ex);
        }
    }
}


