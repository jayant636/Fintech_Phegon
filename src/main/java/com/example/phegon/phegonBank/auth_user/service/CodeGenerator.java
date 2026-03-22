package com.example.phegon.phegonBank.auth_user.service;

import com.example.phegon.phegonBank.auth_user.repository.PasswordResetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
@RequiredArgsConstructor
public class CodeGenerator {

    private final PasswordResetRepository passwordResetRepository;

    private static final String ALPHA_NUMERIC = "ABSNJJNSJNSKJDHJSMDNKWIOERURJFDNDJSSK";
    private static final int CODE_LENGTH = 5;

    public String generateUniqueCode(){
        String code;
        do {
            code = generateRandomCode();
        }while(passwordResetRepository.findByCode(code).isPresent());

        return code;
    }

    private String generateRandomCode(){
        StringBuilder sb = new StringBuilder();
        SecureRandom random = new SecureRandom();

        for(int i =0;i<CODE_LENGTH;i++){
            int index = random.nextInt(ALPHA_NUMERIC.length());
            sb.append(ALPHA_NUMERIC.charAt(index));
        }

        return sb.toString();
    }

}
