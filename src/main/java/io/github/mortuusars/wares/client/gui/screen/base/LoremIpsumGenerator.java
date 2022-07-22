package io.github.mortuusars.wares.client.gui.screen.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class LoremIpsumGenerator {
    private final String[] words = """
            Lorem ipsum dolor sit amet consectetur adipiscing elit
            sed do eiusmod tempor incididunt ut labore et dolore magna aliqua ut enim ad minim
            veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat duis aute irure dolor
            in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur, excepteur sint occaecat cupidatat
            non proident, sunt in culpa qui officia deserunt mollit anim id est laborum""".split(" ");

    public String words(int count){
        List<String> parts = Arrays.stream(words).limit(count).toList();

        Random random = new Random();

        for (int i = 0; i < count - parts.size(); i++) {
            parts.add(words[random.nextInt(0, words.length)]);
        }

        return String.join(" ", parts);
    }

    public String chars(int count){
        List<String> parts = new ArrayList<>();

        int whole = words.length;

        Random random = new Random();

        while (count > 0){
            String word = whole >= 0 ? words[whole--] : words[random.nextInt(0, words.length)];
            String addedWord = word.length() > count ? word.substring(0, count) : word;
            parts.add(addedWord);
            count -= addedWord.length() + 1;
        }

        return String.join(" ", parts);
    }
}
