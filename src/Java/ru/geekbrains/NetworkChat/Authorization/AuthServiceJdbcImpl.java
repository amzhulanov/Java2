package Java.ru.geekbrains.NetworkChat.Authorization;


import Java.ru.geekbrains.NetworkChat.Persistance.UserRepository;
import Java.ru.geekbrains.NetworkChat.User;

import java.sql.SQLException;

public class AuthServiceJdbcImpl implements AuthService {

    private final UserRepository userRepository;


    public AuthServiceJdbcImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean authUser(User user) {//авторизовываю пользователя через userRepository
        String login=user.getLogin();
        User searchUser=null;
        try {
            searchUser=userRepository.findByLogin(login);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String pwd=user.getPassword();
        return pwd !=null&& pwd.equals(searchUser.getPassword());
    }
}
