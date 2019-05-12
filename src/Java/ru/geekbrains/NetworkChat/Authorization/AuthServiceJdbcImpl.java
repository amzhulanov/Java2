package Java.ru.geekbrains.NetworkChat.Authorization;


import Java.ru.geekbrains.NetworkChat.Exception.RegLoginException;
import Java.ru.geekbrains.NetworkChat.Exception.RegPasswordException;
import Java.ru.geekbrains.NetworkChat.Persistance.UserRepository;
import Java.ru.geekbrains.NetworkChat.User;

import javax.security.auth.login.LoginException;
import java.sql.*;


public class AuthServiceJdbcImpl implements AuthService {

    private static UserRepository userRepository;

    public AuthServiceJdbcImpl(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @Override
    public boolean authUser(User user) {//авторизовываю пользователя через userRepository
        String login = user.getLogin();
        User searchUser = null;
        try {
            searchUser = userRepository.findByLogin(login);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String pwd = user.getPassword();
        return pwd != null && pwd.equals(searchUser.getPassword());
    }


    public static boolean registrationUser(String login, String password, String passwordRepeat) throws RegPasswordException, RegLoginException, LoginException, SQLException {
        Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/network_chat?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Asia/Novosibirsk","root","root");
        Statement stmt=con.createStatement();
        if (!password.equals((passwordRepeat))||password.isEmpty()) {//введённые пароли должны совпадать
            throw new RegPasswordException();
        } else if (login.isEmpty()) {//имя не должно быть пустым
            throw new RegLoginException();

        } else if (stmt.executeQuery("select id from users where login='" + login + "'").next() == true) {//имени не должно быть в базе
            throw new LoginException();
        } else {
            PreparedStatement preparedStatement=con.prepareStatement("insert into users (login,password) values(?,?)");
            preparedStatement.setString(1,login);
            preparedStatement.setString(2,password);
            preparedStatement.execute();
            stmt.close();
            preparedStatement.close();
            con.close();
            return true;

        }

    }

}