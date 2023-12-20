package client;

public interface ClientView {

    /**
     * Метод соединения клиента с сервером
     */
    void connection();

    /**
     * Метод отправки сообщений
     * всем подключённым пользователям
     */
    void sendEveryOne();

    /**
     * Метод отправки индивидуальных сообщений
     * (необходимо выбрать подключённого пользователя)
     */
    void sendToSelected();

    /**
     * Метод выхода клиента из чата
     */
    void exit();

    String getMsg();
}
