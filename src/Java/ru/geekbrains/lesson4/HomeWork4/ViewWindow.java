package Java.ru.geekbrains.lesson4.HomeWork4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ViewWindow extends JFrame {
    private ChatController controller;
    public static JTextField message = new JTextField();
    public static JTextArea textArea = new JTextArea();

    public ViewWindow() {
        //this.controller=new EchoController(this);
        controller=new EchoController(this);
        //"слушатель" для обработки действий пользователя
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                controller.sendMessage(message.getText().trim());
                message.setText("");

            }
        };
        setTitle("Chat");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(200, 200, 500, 500);
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        JScrollPane scrollBar = new JScrollPane(textArea);
        JPanel mainPanel = new JPanel();
        JButton buttonEnter = new JButton("Отправить");

        message.addActionListener(actionListener);//при нажатии на Ентер
        buttonEnter.addActionListener(actionListener);//при нажатии на кнопку
        add(scrollBar, BorderLayout.CENTER);//для отображения скроллбара
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));//панель в горизонтальном оформлении
        mainPanel.add(message);//добавляю на нижнюю панель однострочную строку ввода
        mainPanel.add(buttonEnter);//добавляю кнопку Отправить
        add(mainPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

   /* @Override
    public void sendMessage() {
        if (message.getText().trim().length() > 0) {
            textArea.append(message.getText().trim() + "\n");
            message.setText("");
        }
    }

    */


    //метод для передачи текста из строки ввода в основное окно


    /*private void sendMessage(message.getText().trim()) {
        if (message.getText().trim().length() > 0) {
            textArea.append(message.getText().trim() + "\n");
            message.setText("");
        }

    }

     */

}
