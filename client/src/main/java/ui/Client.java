package ui;

public class Client {

    public void run() {
        PreloginUI preloginUI = new PreloginUI();
        PostloginUI postloginUI = new PostloginUI();
        GameplayUI gameplayUI = new GameplayUI();

        int menu = 0;
        String authToken = null;
        int gameID = 0;

        while (true) {
            switch (menu) {
                case 0:
                    authToken = preloginUI.run();
                    if (authToken != null) {
                        menu = 1;
                    }

                case 1:
                    gameID = postloginUI.run(authToken);
                    if (gameID != 0) {
                        menu = 2;
                    }

                case 2:
                    menu = gameplayUI.run();

                default:
                    break;
            }
        }
    }
}
