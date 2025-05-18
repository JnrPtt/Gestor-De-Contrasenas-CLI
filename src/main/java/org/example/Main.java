package org.example;

public class Main {

    public static void main(String[] args) {
        PasswordStore store = new PasswordStore("passwords.json");
        CLIManager cli = new CLIManager(store);
        cli.start();
    }
}
