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

        while (true) {
            switch (menu) {
                case 0:
                    authToken = preloginUI.run();
                    if (authToken != null) {
                        menu = 1;
                    }

                case 1:
                    joinGameRequest = postloginUI.run(authToken);
                    if (joinGameRequest != null) {
                        menu = 2;
                    }

                case 2:
                    gameplayUI.run(joinGameRequest);

                default:
                    break;
            }
        }
    }
}
