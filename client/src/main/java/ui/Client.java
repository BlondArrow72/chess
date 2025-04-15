package ui;

public class Client {

    public void run() {
        // initialize UI objects
        PreLoginUI preLoginUI = new PreLoginUI();
        PostLoginUI postLoginUI = new PostLoginUI();
        GameplayUI gameplayUI = new GameplayUI();

        // initialize ticket objects
        PostLoginTicket postLoginTicket = null;
        GameplayTicket gameplayTicket = null;

        // while loop
        while (true) {
            // enter gameplayUI
            if (gameplayTicket != null) {
                gameplayTicket = gameplayUI.run(gameplayTicket);
            }

            // enter postLoginUI
            else if (postLoginTicket != null) {
                gameplayTicket = postLoginUI.run(postLoginTicket);

                // if no gameplayTicket is returned, go back to preLoginUi
                if (gameplayTicket == null) {
                    postLoginTicket = null;
                }
            }

            // enter preLoginUI
            else {
                postLoginTicket = preLoginUI.run();

                // if postLoginTicket is null, exit program
                if (postLoginTicket == null) {
                    System.exit(0);
                }
            }
        }
    }
}
