import save.Save;
import save.SaveView;
import server.Server;
import server.ServerView;
import server.UiServer;

public class Main {
    public static void main(String[] args) {
        ServerView serverView = new UiServer();
        SaveView saveView = new Save();
        new Server(serverView, saveView);

    }
}
