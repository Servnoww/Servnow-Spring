package servnow.servnow.api.user.service;

import org.springframework.stereotype.Component;
import java.util.Random;

@Component
public class EmailCodeGenerator {

    private static final String CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public String generateCode() {
        StringBuilder code = new StringBuilder();
        Random rnd = new Random();

        for (int i = 0; i < 8; i++) {
            code.append(CHAR_POOL.charAt(rnd.nextInt(CHAR_POOL.length())));
        }

        return code.toString();
    }
}