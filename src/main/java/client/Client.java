package client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket clientSocket;
    private Scanner inMessage;
    private PrintWriter outMessage;

    /**
     * Соединение с сервером
     * @param host адрес хоста
     * @param port порт
     * @param clientName имя клиента
     */
    public void connection(String host, int port, String clientName) {
        try{
            // подключаемся к серверу
            clientSocket = new Socket(host, port);
            outMessage = new PrintWriter(clientSocket.getOutputStream(), true);
            inMessage = new Scanner(clientSocket.getInputStream());
            outMessage.println("#clientsCount#");
            outMessage.println(String.format("#name#%s", clientName));
        }catch (IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Получение принимающего потока
     * @return Scanner
     */
    public Scanner getInMessage() {
        return inMessage;
    }

    /**
     * Отправление сообщения
     * @param outMsg текс сообщения
     */
    public void sendMsg(String outMsg){
        outMessage.println(outMsg);
    }

    /**
     * Выход из чата
     */
    public void exit(){
        try {
            clientSocket.close();
            outMessage.close();
            inMessage.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
