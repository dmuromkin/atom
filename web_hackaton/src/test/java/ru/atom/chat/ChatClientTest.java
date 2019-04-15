package ru.atom.chat;

import okhttp3.Response;
import okhttp3.ResponseBody;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.util.regex.Matcher;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ChatApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc

public class ChatClientTest {
    private static final Logger log = LoggerFactory.getLogger(ChatClientTest.class);

    private static String MY_NAME_IN_CHAT = "I_AM_STUPID";
    private static String MY_MESSAGE_TO_CHAT = "KILL_ME_SOMEONE";
    private static String user = "Dmitry";
    @Autowired
    private MockMvc mockMvc;


    @Test
    public void login() throws Exception {
        String error = "Already logged in:(";
        mockMvc.perform(post("/chat/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("name", MY_NAME_IN_CHAT))
                .andExpect(status().isOk());
        mockMvc.perform(post("/chat/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("name", MY_NAME_IN_CHAT))
                .andExpect(content().string(error));
        /*Response response = ChatClient.login(MY_NAME_IN_CHAT);

        log.info("[" + response + "]");
        String body = response.body().string();
        log.info(body);
        Assert.assertTrue(response.code() == 200 || body.equals("Already logged in:("));
    */
    }

    @Test
    public void viewChat() throws Exception {
       /* Response response = ChatClient.viewChat();
        log.info("[" + response + "]");
        log.info(response.body().string());
        Assert.assertEquals(200, response.code());*/
        mockMvc.perform(post("/chat/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("name", user));
        mockMvc.perform(post("/chat/logout")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("name", user));
       /* String answer="["+user+"] logged in\n" +
                "["+MY_NAME_IN_CHAT+"] logged in";*/
        mockMvc.perform(get("/chat/chat"))
                .andExpect(status().isOk());
    }

    @Test
    public void viewOnline() throws Exception {
        //String user="Dmitry";
        mockMvc.perform(post("/chat/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("name", user));
        mockMvc.perform(get("/chat/online"))
                .andExpect(content().string(user))
                .andExpect(status().isOk());
        mockMvc.perform(post("/chat/logout")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("name", user));
        /*Response response = ChatClient.viewOnline();
        System.out.println("[" + response + "]");
        System.out.println(response.body().string());
        Assert.assertEquals(200, response.code());*/
    }

    @Test
    public void say() throws Exception {
        mockMvc.perform(post("/chat/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("name", user));
        mockMvc.perform(post("/chat/say")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("name", user)
                .param("msg", "Hello"))
                .andExpect(status().isOk());
        mockMvc.perform(post("/chat/logout")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("name", user));
        /*Response response1 = ChatClient.login(MY_NAME_IN_CHAT);
        System.out.println("[" + response1 + "]");
        Response response = ChatClient.say(MY_NAME_IN_CHAT, MY_MESSAGE_TO_CHAT);
        System.out.println("[" + response + "]");
        System.out.println(response.body().string());
        Assert.assertEquals(200, response.code());*/
    }
}
