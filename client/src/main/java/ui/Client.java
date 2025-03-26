package ui;

import model.JoinGameRequest;

public class Client {

    public void run() {
        PreloginUI preloginUI = new PreloginUI();
        PostloginUI postloginUI = new PostloginUI();
        GameplayUI gameplayUI = new GameplayUI();

        int menu = 0;
        String authToken = null;
        JoinGameRequest joinGameRequest = null;

        try {
            while (true) {
                switch (menu) {
                    case 0:
                        authToken = preloginUI.run();
                        if (authToken != null) {
                            menu = 1;
                        }
                        break;

                    case 1:
                        joinGameRequest = postloginUI.run(authToken);
                        if (joinGameRequest.authToken() == null) {
                            authToken = null;
                            menu = 0;
                        }
                        else {
                            menu = 2;
                        }
                        break;

                    case 2:
                        gameplayUI.run(joinGameRequest);
                        menu = 1;
                        break;

                    default:
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            run();
        }
    }
}
