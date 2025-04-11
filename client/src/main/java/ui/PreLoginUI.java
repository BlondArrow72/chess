package ui;

import requests.LoginRequest;
import requests.RegisterRequest;
import responses.LoginResponse;
import responses.RegisterResponse;
import serverfacade.ServerFacade;

import java.util.Scanner;

public class PreLoginUI {
    private final Scanner scanner = new Scanner(System.in);
    private final ServerFacade serverFacade = new ServerFacade(8080);

    public PostLoginTicket run() {
        while (true) {
            // let user know what they can do
            printMenu();

            // get userResponse
            String userResponse = scanner.nextLine();

            // switch on userResponse
            switch (userResponse) {
                case "Login"    -> {return login();}
                case "Register" -> {return register();}
                case "Quit"     -> {return null;}
                case "Help"     -> help();
                default -> System.out.println("Invalid Response.");
            }
        }
    }

    private void printMenu() {
        System.out.println("Choose an option:");
        System.out.println();

        System.out.println("Login");
        System.out.println("Register");
        System.out.println("Quit");
        System.out.println("Help");
        System.out.println();

        System.out.println("Hit ENTER after typing your selection.");
    }

    private PostLoginTicket login() {
        // get username and password
        System.out.println("Enter your username:");
        String username = scanner.nextLine();

        System.out.println("Enter your password:");
        String password = scanner.nextLine();

        // make login request
        LoginRequest loginRequest = new LoginRequest(username, password);
        LoginResponse loginResponse = serverFacade.login(loginRequest);

        // return postLoginTicket
        return new PostLoginTicket(loginResponse.authToken());
    }

    private PostLoginTicket register() {
        // get username, password, and email
        System.out.println("Enter your username:");
        String username = scanner.nextLine();

        System.out.println("Enter your password:");
        String password = scanner.nextLine();

        System.out.println("Enter your email:");
        String email = scanner.nextLine();

        // make register request
        RegisterRequest  registerRequest  = new RegisterRequest(username, password, email);
        RegisterResponse registerResponse = serverFacade.register(registerRequest);

        // return postLoginTicket
        return new PostLoginTicket(registerResponse.authToken());
    }

    private void help() {
        System.out.println("Help Menu:");
        System.out.println("Login - Prompts the user to input login information.");
        System.out.println("Register - Prompts the user to input registration information.");
        System.out.println("Quit - Exits the program.");
        System.out.println("Help - Displays help text.");
        System.out.println();
        run();
    }
}
