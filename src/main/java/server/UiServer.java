package server;

import client.UiClient;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class UiServer extends JFrame implements ServerView{

    private final Path pathAllClients = Path.of("src/main/resources/allClients.txt");
    private final JTextArea jtaClients;
    private final JTextArea jtaTechInfo;

    /**
     * Конструктор
     */
    public UiServer() {

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setBounds(700, 100, 600, 400);
        frame.setResizable(false);
        frame.setTitle("Сервер");

        Container cont = new Container();

        JLabel jlAllClients = new JLabel("Все клиенты:");
        jlAllClients.setBounds(10,5,280,25);

        JLabel jlInfo = new JLabel("Техническая информация:");
        jlInfo.setBounds(295,5,280,25);

        jtaClients = new JTextArea();
        jtaClients.setBounds(0, 0, 280, 250);
        jtaClients.setEditable(false);

        JScrollPane jspClients = new JScrollPane(jtaClients);
        jspClients.setBounds(10,30,280,250);
        jspClients.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        jtaTechInfo = new JTextArea();
        jtaTechInfo.setBounds(0, 0, 280, 250);
        jtaTechInfo.setEditable(false);

        JScrollPane jspTechInfo = new JScrollPane(jtaTechInfo);
        jspTechInfo.setBounds(295,30,280,250);
        jspTechInfo.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        JButton jbNewClient = new JButton("Новый собеседник");
        jbNewClient.setBounds(10,290,150,25);

        JButton jbExit = new JButton("Остановить сервер");
        jbExit.setBounds(425,290,150,25);

        cont.add(jlAllClients, 0);
        cont.add(jlInfo);
        cont.add(jspClients);
        cont.add(jspTechInfo);
        cont.add(jbNewClient);
        cont.add(jbExit);
        cont.setVisible(true);
        frame.add(cont);
        frame.setVisible(true);

        jbNewClient.addActionListener(x -> new UiClient());

        jbExit.addActionListener(x -> {
            try {
                BufferedWriter bf = Files.newBufferedWriter(pathAllClients, StandardOpenOption.TRUNCATE_EXISTING);
                bf.flush();
                bf.close();
            }catch (IOException ex){
                System.out.println(ex.getMessage());
            }
            System.exit(0);
        });
    }

    @Override
    public JTextArea getJtaClients() {
        return jtaClients;
    }

    @Override
    public JTextArea getJtaTechInfo() {
        return jtaTechInfo;
    }
}