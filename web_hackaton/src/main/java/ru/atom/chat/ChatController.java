package ru.atom.chat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;


import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;
import java.sql.SQLException;


@EntityScan
@Controller
@RequestMapping("chat")
public class ChatController {

    private static final Logger log = LoggerFactory.getLogger(ChatController.class);
    private boolean connection = false;
    private Queue<String> messages = new ConcurrentLinkedQueue<>();
    private Map<String, String> usersOnline = new ConcurrentHashMap<>();
    private int UserId;
    private int[] count_attempts = new int[20];
    static ArrayList<String> Users = new ArrayList();
    static long[] timings = new long[20];
    //System.out.println("Testing connection to PostgreSQL JDBC");
    /**
     * curl -X POST -i localhost:8080/chat/login -d "name=I_AM_STUPID"
     */


    @RequestMapping(
            path = "login",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> login(@RequestParam("name") String name) {
        if (name.length() < 1) {
            return ResponseEntity.badRequest().body("Too short name, sorry :(");
        }
        if (name.length() > 20) {
            return ResponseEntity.badRequest().body("Too long name, sorry :(");
        }
        if (usersOnline.containsKey(name)) {
            return ResponseEntity.badRequest().body("Already logged in:(");
        }
        if(connection==false)
        {
            PostSQLBase baseC = new PostSQLBase();
            ArrayList<String> history = new ArrayList<String>();
            history=baseC.getdata();
            for(String message : history)
                messages.add(message);
        }
        connection=true;
        usersOnline.put(name, name);
        Users.add(name);
        messages.add("[" + name + "] logged in");

        return ResponseEntity.ok().build();
    }

    /**
     * curl -i localhost:8080/chat/chat
     */
    @RequestMapping(
            path = "chat",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public ResponseEntity chat() {
        String responseBody = String.join("\n", messages);

        return ResponseEntity.ok(responseBody);
    }

    /**
     * curl -i localhost:8080/chat/online
     */
    @RequestMapping(
            path = "online",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity online() {
        String responseBody = String.join("\n", usersOnline.keySet().stream().sorted().collect(Collectors.toList()));
        return ResponseEntity.ok(responseBody);
    }

    /**
     * curl -X POST -i localhost:8080/chat/logout -d "name=I_AM_STUPID"
     */
    @RequestMapping(
            path = "logout",
            method = RequestMethod.POST,
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public ResponseEntity logout(@RequestParam("name") String name) {
        if (usersOnline.containsKey(name)) {
            usersOnline.remove(name);
            messages.add("[" + name + "] logged out");
            return ResponseEntity.ok("User " + name + " successfully logged out!");
        }

        return ResponseEntity.badRequest().body("User " + name + " is not exists");
    }



    /**
     * curl -X POST -i localhost:8080/chat/say -d "name=I_AM_STUPID&msg=Hello everyone in this chat"
     */
    @RequestMapping(
            path = "say",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity say(@RequestParam("name") String name, @RequestParam("msg") String msg) throws SQLException {
        /*PostSQLBase ss =new PostSQLBase();
        ss.chat_say(name,msg);*/
        SimpleDateFormat sdfDate = new SimpleDateFormat(" HH:mm:ss");
        Date now = new Date();
        String strDate = sdfDate.format(now);
        if (usersOnline.containsKey(name)) {
            UserId = Users.indexOf(name);
            if (notspam(UserId) == false) {
                messages.add(strDate + " (" + name + "): msg blocked");
                return ResponseEntity.badRequest().body("(" + name + ") msg blocked");
            } else {
                messages.add(strDate + " (" + name + "): " + msg);
                PostSQLBase baseC = new PostSQLBase();
                baseC.chat_say(name,msg);
                return ResponseEntity.ok(strDate + " (" + name + "): " + msg);
            }
        } else {
            return ResponseEntity.badRequest().body("User with name " + name + " is not found");
        }
    }

    public boolean notspam(int ID) {
        boolean result = true;
        long last_msg = System.currentTimeMillis();
        if (timings[ID] == 0) {
            timings[ID] = System.currentTimeMillis();
            count_attempts[ID] += 1;
        } else {
            count_attempts[ID] += 1;
            last_msg = timings[ID];
            timings[ID] = System.currentTimeMillis();
            if (count_attempts[ID] > 3) {
                long difference = (timings[ID] - last_msg);
                if (difference < 3000)
                    result = false;
                else
                    count_attempts[ID] = 1;
            }
        }
        return result;

    }
}
// cd C:\curl-7.64.0-win64-mingw\bin
//curl -X POST -i localhost:8080/chat/login -d "name=I_AM_STUPID"
/**
 * curl -X POST -i localhost:8080/chat/say -d "name=I_AM_STUPID&msg=Hello everyone in this chat"
 */
//Hibernate postgresql