package ru.atom.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChatApplication {
    public static void main(String[] args) {
       // PostSQLBase baseC = new PostSQLBase();
        //baseC.getdata();
       // baseC.getDBConnection();
       // baseC.chat_say("Dmitry","Hello");
        SpringApplication.run(ChatApplication.class, args);
    }
}
