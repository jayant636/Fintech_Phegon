package com.example.phegon.phegonBank.notification.service;

import com.example.phegon.phegonBank.auth_user.entity.User;
import com.example.phegon.phegonBank.enums.Notificationtype;
import com.example.phegon.phegonBank.notification.dtos.NotificationDto;
import com.example.phegon.phegonBank.notification.entity.NotificationEntity;
import com.example.phegon.phegonBank.notification.repository.NotificationRepository;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService{

    private final NotificationRepository notificationRepository;
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;


    @Override
    @Async
    public void sendEmail(NotificationDto notificationDto, User user) {

        try{

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(
                    mimeMessage,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name()
            );
            mimeMessageHelper.setTo(notificationDto.getRecipient());
            mimeMessageHelper.setSubject(notificationDto.getSubject());

//            Use Template if provided
            if(notificationDto.getTemplateName() != null){
                Context context = new Context();
                context.setVariables(notificationDto.getTemplateVariable());
                String htmlContent = templateEngine.process(notificationDto.getTemplateName(),context);
                mimeMessageHelper.setText(htmlContent,true);
            }else{
//                if no template send text body directly
                mimeMessageHelper.setText(notificationDto.getBody(),true);

            }

            javaMailSender.send(mimeMessage);

            NotificationEntity notificationToSave = NotificationEntity.builder()
                    .recipient(notificationDto.getRecipient())
                    .subject(notificationDto.getSubject())
                    .body(notificationDto.getBody())
                    .type(Notificationtype.EMAIL)
                    .user(user)
                    .build();

            notificationRepository.save(notificationToSave);

        }catch (Exception e){
            log.error(e.getMessage());
        }
    }
}
