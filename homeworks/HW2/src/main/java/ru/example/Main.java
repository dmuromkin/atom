package ru.example;

import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(Main.class.getName());

    public static int bulls;
    public static int cows;

    public static boolean Game_Core(String user_word, String Answer) {
        bulls = 0;
        cows = 0;
        boolean result = false;
        for (int i = 0; i < Answer.length(); i++) {
            if (user_word.toCharArray()[i] == Answer.toCharArray()[i])
                bulls++;
        }
        if (bulls == Answer.length())
            result = true;
        else {
            char letter;
            boolean[] GameFlag;
            boolean[] PlayerFlag;

            GameFlag = new boolean[Answer.length()];
            PlayerFlag = new boolean[user_word.length()];
            for (int i = 0; i < Answer.length(); i++) {
                if (Answer.toCharArray()[i] == user_word.toCharArray()[i]) {
                    GameFlag[i] = false;
                    PlayerFlag[i] = false;
                } else {
                    GameFlag[i] = true;
                    PlayerFlag[i] = true;
                }

            }
            for (int i = 0; i < user_word.length(); i++) {
                letter = user_word.toCharArray()[i];
                if (user_word.indexOf(letter) != -1 && PlayerFlag[i]) {
                    for (int j = 0; j < Answer.length(); j++) {
                        if (letter == Answer.toCharArray()[j] && GameFlag[j]) {
                            PlayerFlag[i] = false;
                            GameFlag[j] = false;
                            cows++;
                            break;
                        }
                    }
                }
            }
            System.out.printf("bulls:%d cows:%d\n", bulls, cows);
        }
        log.info("User word: " + user_word + "   Bulls: " + bulls + "   Cows: " + cows);
        return result;
    }

    public static void main(String[] args) {
        int count = 0;
        log.info("Game started!");//logger works
        File path = new File("dictionary.txt");
        if (!path.exists())
            System.out.println("File not found");
        ArrayList<String> arr = new ArrayList<String>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {

            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                arr.add(sCurrentLine);
                count++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        ////////GAME///////
        //System.out.printf("count=%d",count);
        Random rnd = new Random(System.currentTimeMillis());
        int number = rnd.nextInt(count);
        String answer = arr.get(number);
        System.out.println(answer);
        log.info("Right answer: " + answer);
        boolean result = false;
        int attempts = 10;
        System.out.println("Make sure u switched English");
        System.out.printf("There are %d letters in my word\n", answer.length());
        while ((!result) && (attempts != 0)) {
            attempts--;
            System.out.println("Your guess?");
            Scanner input = new Scanner(System.in);
            String user_word = input.nextLine();
            if (user_word.length() != answer.length()) {
                System.out.println("Incorrect word length!");
                System.out.printf("There are %d letters in my word\n", answer.length());
                log.info("Incorrect word length: " + user_word.length() + "/" + answer.length());
                attempts++;
            } else
                result = Game_Core(user_word, answer);
            System.out.printf("Attempts remaining:%d\n", attempts);
        }
        if (!result) {
            System.out.printf("You lost( My word is %s\n", answer);
            log.info("Defeat!");
        } else {
            System.out.println("You won!");
            log.info("Win!");
        }
    }
}