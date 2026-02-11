package com.example.phegon.phegonBank;

import com.example.phegon.phegonBank.auth_user.entity.User;
import com.example.phegon.phegonBank.enums.Notificationtype;
import com.example.phegon.phegonBank.notification.dtos.NotificationDto;
import com.example.phegon.phegonBank.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
//@RequiredArgsConstructor
public class PhegonBankApplication {

//	private final NotificationService notificationService;

	public static void main(String[] args) {
		SpringApplication.run(PhegonBankApplication.class, args);
	}

//	@Bean
//	CommandLineRunner commandLineRunner(){
//		return args -> {
//			NotificationDto notificationDto = NotificationDto.builder()
//					.recipient("jayantjalandra@gmail.com")
//					.subject("Testing")
//					.body("This is a test email")
//					.type(Notificationtype.EMAIL)
//					.build();
//
//			notificationService.sendEmail(notificationDto,new User());
//		};
//	}

}
