package client;

import tag.Tag;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class UiClient extends JFrame implements ClientView {

    //region Переменные
    private Client client;
    private Scanner in;
    private final Path pathAllClients = Path.of("src/main/resources/allClients.txt");
    private final DefaultListModel<String> model;
    private final Container startCont;
    private final Container baseCont;
    private final JTextField jtfHost;
    private final JTextField jtfPort;
    private final JTextField jtfName;
    private final JTextField jtfInfo;
    private final JTextArea jtaMsg;
    private final JList<String> jListNames;
    private final JTextField jtfMsg;
    //endregion

    //region Конструктор
    /**
     * Конструктор
     */
    public UiClient() {
        model = new DefaultListModel<>();
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBounds(100, 100, 600, 400);
        setTitle("Клиент");

        // Стартовое окно
        startCont = new Container();

        JLabel jlHost = new JLabel("Адрес сервера");
        jlHost.setBounds(5, 5, 100, 25);

        jtfHost = new JTextField("127.0.0.1");
        jtfHost.setBounds(110, 5, 100, 25);
        jtfHost.setEditable(false);

        JLabel jlPort = new JLabel("Порт");
        jlPort.setBounds(5, 45, 100, 25);

        jtfPort = new JTextField("5555");
        jtfPort.setBounds(110, 45, 100, 25);
        jtfPort.setEditable(false);

        JLabel jlName = new JLabel("Имя клиента");
        jlName.setBounds(5, 85, 100, 25);

        jtfName = new JTextField();
        jtfName.setBounds(110, 85, 100, 25);

        JButton jbConnect = new JButton("Подключить");
        jbConnect.setBounds(15, 150, 150, 25);

        startCont.add(jlHost);
        startCont.add(jtfHost);
        startCont.add(jlPort);
        startCont.add(jtfPort);
        startCont.add(jlName);
        startCont.add(jtfName);
        startCont.add(jbConnect);

        // Основное окно
        baseCont = new Container();
        jtfInfo = new JTextField();
        jtfInfo.setBounds(5, 5, 400, 25);

        jtaMsg = new JTextArea();
        jtaMsg.setBounds(0, 0, 400, 200);
        jtaMsg.setEditable(false);

        JScrollPane jspMsg = new JScrollPane(jtaMsg);
        jspMsg.setBounds(5, 45, 400, 200);
        jspMsg.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        jListNames = new JList<>();
        jListNames.setBounds(0, 0, 165, 275);
        jListNames.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JScrollPane jspNames = new JScrollPane(jListNames);
        jspNames.setBounds(415, 5, 165, 275);
        jspNames.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        JButton jbSendToSelected = new JButton("Отправить выделенному");
        jbSendToSelected.setBounds(165, 300, 200, 25);

        jtfMsg = new JTextField();
        jtfMsg.setBounds(5, 255, 400, 25);

        JButton jbSendEveryOne = new JButton("Отправить всем");
        jbSendEveryOne.setBounds(5, 300, 150, 25);

        JButton jbExit = new JButton("Выход");
        jbExit.setBounds(425, 300, 150, 25);

        baseCont.add(jtfInfo);
        baseCont.add(jspMsg);
        baseCont.add(jspNames);
        baseCont.add(jbSendToSelected);
        baseCont.add(jtfMsg);
        baseCont.add(jbSendEveryOne);
        baseCont.add(jbExit);

        add(startCont);
        startCont.setVisible(true);
        jtfName.grabFocus();

        setVisible(true);

        jbConnect.addActionListener(x -> connection());
        jbSendEveryOne.addActionListener(x -> sendEveryOne());
        jbSendToSelected.addActionListener(x -> sendToSelected());
        jbExit.addActionListener(x -> exit());
    }
    //endregion

    @Override
    public void connection(){
        boolean start = !jtfName.getText().isEmpty();
        while (start) {
            client = new Client();
            String host = jtfHost.getText();
            int port = Integer.parseInt(jtfPort.getText());
            String clientName = getClientName();
            setTitle(String.format("Клиент - %s", getClientName()));
            remove(startCont);
            add(baseCont);
            baseCont.setVisible(true);
            setVisible(true);
            client.connection(host, port, clientName);
            in = client.getInMessage();
            start = false;
        }
        // в отдельном потоке начинаем работу с сервером
        new Thread(() -> {
            try {
                while (true) {
                    // если есть входящее сообщение от сервера
                    if (in.hasNext()) {
                        // считываем его
                        String inMsg = in.nextLine();
                        String nameClient = getClientName();
                        String result;

                        // Если пришло сообщение о количестве клиентов
                        if (Tag.isClientsCount(inMsg)) {
                            result = inMsg.replaceAll("#clientsCount#", "");
                            if (Integer.parseInt(result) == 1)
                                jtfInfo.setText("Собеседников пока нет.");
                            else jtfInfo.setText("Количество собеседников: " + result);
                        }

                        // Если пришло сообщение для всех
                        if (Tag.isMsg(inMsg)) {
                            result = inMsg.replaceAll("#msg#", "");
                            if(!inMsg.contains(nameClient)) jtaMsg.append(result + "\n");
                            else {
                                result = result.replaceAll(nameClient, "Я");
                                jtaMsg.append(result + "\n");
                            }
                        }

                        // Если пришло имя клиента
                        if(Tag.isName(inMsg)){
                            model.removeAllElements();
                            model.addAll(Files.readAllLines(pathAllClients));
                            jListNames.setModel(model);
                        }

                        // Если пришло сообщение о выходе клиента из чата
                        if(Tag.isExit(inMsg)){
                            model.removeAllElements();
                            model.addAll(Files.readAllLines(pathAllClients));
                            jListNames.setModel(model);
                        }
                    }
                }
            } catch (Exception ignored) {}
        }).start();
    }

    @Override
    public void sendEveryOne(){
        if (!getMsg().isEmpty()) {
            String msg = String.format("#msg# %s: %s", getClientName(), getMsg());
            client.sendMsg(msg);
            jtfMsg.setText("");
        }
    }


    // Пока не доделано (суть в выделении элемента в jListNames)
    @Override
    public void sendToSelected(){
        if (!getMsg().isEmpty()) {
            String msg = String.format("#msgSelected#%s:%s", getClientName(), getMsg());
            client.sendMsg(msg);
            jtfMsg.setText("");
        }
    }

    @Override
    public void exit(){
        client.sendMsg(String.format("#exit#%s", getClientName()));
        client.exit();
        dispose();
    }

    @Override
    public String getMsg(){
        return jtfMsg.getText();
    }

    /**
     * Получение имени клиента
     * @return String
     */
    public String getClientName(){
        return jtfName.getText();
    }
}