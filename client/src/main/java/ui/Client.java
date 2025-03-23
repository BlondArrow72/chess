package ui;

import model.AuthData;

public class Client {
    int menu = 0;

    public void run() {
        PreloginUI preloginUI = new PreloginUI();
        PostloginUI postloginUI = new PostloginUI();
        GameplayUI gameplayUI = new GameplayUI();

        AuthData userAuth;

        while (true) {
            switch (menu) {
                case 0:
                    userAuth = preloginUI.run();
                    if (userAuth == null) {
                        break;
                    }
                    else {
                        menu = 1;
                    }

                case 1:
                    menu = postloginUI.run();

                case 2:
                    menu = gameplayUI.run();

                default:
                    break;
            }
        }
    }
}
