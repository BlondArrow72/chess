package ui;

import model.LoginRequest;
import model.UserData;
import serverFacade.ServerFacade;

import java.util.Scanner;

public class PreloginUI {
    private final Scanner scanner = new Scanner(System.in);
    private final ServerFacade serverFacade = new ServerFacade();

    public String run() {
        String authToken = null;

        printMenu();

        String userResponse = scanner.nextLine();

        switch(userResponse) {
            case ("Login"):
                authToken = login();

            case ("Register"):
                authToken = register();

            case ("Quit"):
                break;

            case ("Help"):
                help();

            default:
                System.out.println("Invalid Response.");
        }

        return authToken;
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

    private String login() {
        // get username and password
        System.out.println("Enter your username:");
        String username = scanner.nextLine();

        System.out.println("Enter your password:");
        String password = scanner.nextLine();

        // make login request
        LoginRequest loginRequest = new LoginRequest(username, password);
        return serverFacade.login(loginRequest).authToken();
    }

    private String register() {
        // get username, password, and email
        System.out.println("Enter your username:");
        String username = scanner.nextLine();

        System.out.println("Enter your password:");
        String password = scanner.nextLine();

        System.out.println("Enter your email:");
        String email = scanner.nextLine();

        // make register request
        UserData newUser = new UserData(username, password, email);
        return serverFacade.register(newUser).authToken();
    }

    private void help() {
        System.out.println("Help Menu:");
        System.out.println("Login - Prompts the user to input login information.");
        System.out.println("Register - Prompts the user to imput registration information.");
        System.out.println("Quit - Exits the program.");
        System.out.println("Help - Displays help text.");
        System.out.println();
    }
}
