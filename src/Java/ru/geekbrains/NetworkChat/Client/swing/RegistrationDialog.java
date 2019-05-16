package Java.ru.geekbrains.NetworkChat.Client.swing;

import Java.ru.geekbrains.NetworkChat.Server.Exception.RegPasswordException;
import Java.ru.geekbrains.NetworkChat.Client.Network;

import javax.security.auth.login.LoginException;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class RegistrationDialog extends JDialog {
    private Network network;
    private JTextField tfUsername;
    private JPasswordField pfPassword;
    private JPasswordField pfPasswordRepeat;
    private JLabel lbUsername;
    private JLabel lbPassword;
    private JButton btnCancel;
    private JButton btnRegistration;

    private boolean registration=false;

    public RegistrationDialog(Frame parent, Network network){


        super(parent, "Регистрация", true);
        this.network = network;
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();



        cs.fill = GridBagConstraints.VERTICAL;

        lbUsername = new JLabel("Имя пользователя: ");
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        panel.add(lbUsername, cs);

        tfUsername = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 0;
        cs.gridwidth = 2;
        panel.add(tfUsername, cs);

        lbPassword = new JLabel("Пароль: ");
        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 1;
        panel.add(lbPassword, cs);

        pfPassword = new JPasswordField(20);
        cs.gridx = 1;
        cs.gridy = 1;
        cs.gridwidth = 2;
        panel.add(pfPassword, cs);
        panel.setBorder(new LineBorder(Color.GRAY));

        lbPassword = new JLabel("Повторите пароль: ");
        cs.gridx = 0;
        cs.gridy = 2;
        cs.gridwidth = 1;
        panel.add(lbPassword, cs);

        pfPasswordRepeat = new JPasswordField(20);
        cs.gridx = 1;
        cs.gridy = 2;
        cs.gridwidth = 2;
        panel.add(pfPasswordRepeat, cs);
        panel.setBorder(new LineBorder(Color.GRAY));

        btnRegistration = new JButton("Зарегистрироваться");
        btnCancel = new JButton("Отмена");

        JPanel bp = new JPanel();
        bp.add(btnRegistration);
        btnRegistration.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               // try {//Регистрация при нажатии на кнопку Зарегистрироваться
                    //AuthService
                    try {
                        network.registration(tfUsername.getText(),String.valueOf(pfPassword.getPassword()),String.valueOf(pfPasswordRepeat.getPassword()));
                    } catch(LoginException ex){
                        JOptionPane.showMessageDialog(RegistrationDialog.this,
                                "Имя занято",
                                "Регистрация",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }catch(RegPasswordException ex){
                        JOptionPane.showMessageDialog(RegistrationDialog.this,
                                "Пароли не совпадают или пустые",
                                "Регистрация",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }catch (IOException ex){
                        ex.printStackTrace();
                        /*catch(RegLoginException ex){
                        JOptionPane.showMessageDialog(RegistrationDialog.this,
                                "Имя не указано",
                                "Регистрация",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }*/
                    }

                    registration = true;
                   /* authService.registrationUser(tfUsername.getText(),String.valueOf(pfPassword.getPassword()),String.valueOf(pfPasswordRepeat.getPassword()));
                    JOptionPane.showMessageDialog(RegistrationDialog.this,
                            "Вы успешно зарегистрировались",
                            "Регистрация",
                            JOptionPane.INFORMATION_MESSAGE );
                    registration = true;
                } catch(LoginException | SQLException ex){
                    JOptionPane.showMessageDialog(RegistrationDialog.this,
                            "Имя занято",
                            "Регистрация",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }catch(RegPasswordException ex){
                    JOptionPane.showMessageDialog(RegistrationDialog.this,
                            "Пароли не совпадают или пустые",
                            "Регистрация",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }catch(RegLoginException ex){
                    JOptionPane.showMessageDialog(RegistrationDialog.this,
                            "Имя не указано",
                            "Регистрация",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }*/

                dispose();//закрываю окно регистрации
            }
        });

        bp.add(btnCancel);
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registration= false;
                dispose();//закрываю окно ввода логина/пароля
            }
        });

        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(bp, BorderLayout.PAGE_END);

        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }
}
