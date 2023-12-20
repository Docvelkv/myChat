package server;

import javax.swing.*;

public interface ServerView {

    /**
     * Получение компонента для изменения
     * @return JTextArea
     */
    JTextArea getJtaClients();

    /**
     * Получение компонента для изменения
     * @return JTextArea
     */
    JTextArea getJtaTechInfo();
}
