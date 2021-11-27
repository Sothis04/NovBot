package fr.novlab.bot.commands.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Manager {

    public static void loadManager() throws IOException {

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));

        String name = reader.readLine();

        if(name.contains("/setActivity")) {
            String args = name.substring(12);
            SetActivity.execute(args.trim());
        } else {
            System.out.println("Unknown Command");
        }
    }
}
