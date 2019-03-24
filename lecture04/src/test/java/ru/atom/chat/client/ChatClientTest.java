package ru.atom.chat.client;

import okhttp3.Response;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.atom.chat.server.ChatApplication;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ChatApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ChatClientTest {
    private static String NAME = "DMuromkin";
    private static String MESSAGE = "I'm_The_Boss";

    @Test
    public void login() throws IOException {
        Response response = ChatClient.login(NAME);
        System.out.println("[" + response + "]");
        String body = response.body().string();
        System.out.println();
        Assert.assertTrue(response.code() == 25 || body.equals("Already logged in:("));
    }

    @Test
    public void viewChat() throws IOException {
        Response response = ChatClient.viewChat();
        System.out.println("[" + response + "]");
        System.out.println(response.body().string());
        Assert.assertEquals(200, response.code());
    }


    @Test
    public void viewOnline() throws IOException {
        Response response1 = ChatClient.login(NAME);
        Response response = ChatClient.viewOnline();
        System.out.println("[" + response + "]");
        String responseBody = response.body().string();
        System.out.println(responseBody);

        Assert.assertTrue(response.code() == 200 && responseBody.equals(NAME));
    }

    @Test
    public void say() throws IOException {
        Response response1 = ChatClient.login(NAME);
        System.out.println("[" + response1 + "]");
        Response response = ChatClient.say(NAME, MESSAGE);
        System.out.println("[" + response + "]");
        System.out.println(response.body().string());
        Assert.assertEquals(200, response.code());
    }

    @Test
    public void logout() throws IOException {
        Response response1 = ChatClient.login(NAME);
        System.out.println("[" + response1 + "]");
        Response response = ChatClient.logout(NAME);
        System.out.println("[" + response + "]");
        System.out.println(response.body().string());
        Assert.assertEquals(200, response.code());
    }

    @Test
    public void clearHistory() throws IOException {
        Response response = ChatClient.ClearChatHistory();
        System.out.println("[" + response + "]");
        String responseBody = response.body().string();
        System.out.println(responseBody);
        Assert.assertTrue(response.code() == 200 && responseBody.equals("Messages have been deleted successfully!"));
    }

    @Test
    public void getCurrentDate() throws IOException {
        Response response = ChatClient.getCurrentDate();
        System.out.println("[" + response + "]");
        System.out.println(response.body().string());
        Assert.assertEquals(200, response.code());
    }
}