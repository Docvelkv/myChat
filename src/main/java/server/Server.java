package server;

import save.SaveView;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    protected ServerView serverView;
    protected SaveView saveView;
    private Socket clientSocket;
    private ServerSocket serverSocket;
    static final int PORT = 5555;
    private final List<ClientHandler> listAllClients = new ArrayList<>();

    /**
     * Конструктор
     * @param serverView интерфейс
     * @param saveView интерфейс
     */
    public Server(ServerView serverView, SaveView saveView) {
        this.serverView = serverView;
        this.saveView = saveView;
        start();
    }

    /**
     * Запуск сервера
     */
    public void start(){
        try {
            serverSocket = new ServerSocket(PORT);
            serverView.getJtaTechInfo()
                    .append(String.format("Сервер запущен. Порт: %d\n", serverSocket.getLocalPort()));

            //noinspection InfiniteLoopStatement
            while (true) {
                // подключение к серверу
                clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(this, clientSocket);
                listAllClients.add(clientHandler);
                serverView.getJtaClients().setText("");
                listAllClients.forEach(x -> serverView.getJtaClients().append(x.toString() + "\n"));
                // каждое подключение клиента обрабатываем в новом потоке
                new Thread(clientHandler).start();
            }
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        finally {
            try {
                // закрываем подключение
                if(clientSocket != null){
                    clientSocket.close();
                    System.out.println("Сервер остановлен");
                    serverSocket.close();
                }
            }
            catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    /**
     * Получение списка подключённых клиентов
     * @return List<ClientHandler>
     */
    public List<ClientHandler> getListAllClients() {
        return listAllClients;
    }

    /**
     * Отправка сообщения всем клиентам
     * @param msg текст сообщения
     */
    public void sendMsgToAllClients(String msg) {
        for (ClientHandler client : listAllClients) {
            client.sendMsg(msg);
        }
    }

    /**
     * Удаление клиента из списка
     * @param client Клиент
     */
    public void removeClient(ClientHandler client) {
        listAllClients.remove(client);
    }
}
