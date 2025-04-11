package ui;

public class Client {

    public void run() {
        // initialize UI objects
        PreloginUI preloginUI = new PreloginUI();
        PostloginUI postloginUI = new PostloginUI();
        GameplayUI gameplayUI = new GameplayUI();

        // initialize ticket objects
        PostloginTicket postloginTicket = null;
        GameplayTicket gameplayTicket = null;

        // while loop
        while (true) {
            // enter gameplayUI
            if (gameplayTicket != null) {
                gameplayUI.run(gameplayTicket);
            }

            // enter postloginUI
            else if (postloginTicket != null) {
                postloginUI.run(postloginUI);
            }

            // enter preloginUI
            else {
                preloginUI.run();
            }
        }
    }
}
