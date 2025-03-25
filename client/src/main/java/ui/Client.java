package ui;

public class Client {

    public void run() {
        PreloginUI preloginUI = new PreloginUI();
        PostloginUI postloginUI = new PostloginUI();
        GameplayUI gameplayUI = new GameplayUI();

        int menu = 0;
        String authToken = null;

        while (true) {
            switch (menu) {
                case 0:
                    authToken = preloginUI.run();
                    if (authToken == null) {
                        break;
                    }
                    menu = 1;

                case 1:
                    postloginUI.run(authToken);

                case 2:
                    menu = gameplayUI.run();

                default:
                    break;
            }
        }
    }
}
