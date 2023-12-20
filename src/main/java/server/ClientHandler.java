package server;

import tag.Tag;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Path;
import java.util.Scanner;

public class ClientHandler implements Runnable {

    private Server server;

    private PrintWriter outMessage;

    private Scanner inMessage;

    private final Path pathAllClients = Path.of("src/main/resources/allClients.txt");

    private static int clientsCount = 0;

    /**
     * Конструктор
     * @param server сервер
     * @param socket канал клиента
     */
    public ClientHandler(Server server, Socket socket) {
        try {
            clientsCount++;
            this.server = server;
            this.outMessage = new PrintWriter(socket.getOutputStream(), true);
            this.inMessage = new Scanner(socket.getInputStream());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Переопределение метода run() (вызов Thread(client).start())
     */
    @Override
    public void run() {
        //noinspection LoopStatementThatDoesntLoop
        while (true) {
            // сервер отправляет сообщение о количестве клиентов
            server.sendMsgToAllClients("#clientsCount#" + clientsCount);
            break;
        }

        // Получение сообщения от клиента и
        // в зависимости от тега выполнение действий
        //noinspection InfiniteLoopStatement
        while (true) {
            if (inMessage.hasNext()) {
                String msgFromClient = inMessage.nextLine();
                // при отправке данного сообщения клиент выходит из чата
                if (Tag.isExit(msgFromClient)) {
                    String result = msgFromClient.replaceAll("#exit#", "");
                    server.saveView.delFromFile(pathAllClients, result);
                    server.removeClient(this);
                    server.serverView.getJtaClients().setText("");
                    server.getListAllClients()
                            .forEach(x -> server.serverView.getJtaClients().append(x.toString()));
                    server.serverView.getJtaTechInfo()
                            .append(String.format("От нас ушёл %s\n", result));
                    server.sendMsgToAllClients(msgFromClient);
                    this.close();
                }

                if(Tag.isName(msgFromClient)){
                    server.sendMsgToAllClients(msgFromClient);
                    String result = msgFromClient.replaceAll("#name#", "");
                    server.saveView.saveToFile(pathAllClients, result);
                    server.serverView.getJtaTechInfo()
                            .append(String.format("К нам присоединился %s\n", result));
                }

                if(Tag.isMsg(msgFromClient)){
                    server.sendMsgToAllClients(msgFromClient);
                }
            }
        }
    }

    /**
     * Отправка сообщения
     * @param msg текст сообщения
     */
    public void sendMsg(String msg) {
        outMessage.println(msg);
    }

    /**
     * Выход клиента из чата
     */
    public void close() {
        server.removeClient(this);
        clientsCount--;
        server.sendMsgToAllClients("#clientsCount#" + clientsCount);
    }
}