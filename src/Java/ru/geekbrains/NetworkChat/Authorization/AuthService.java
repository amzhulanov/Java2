package Java.ru.geekbrains.NetworkChat.Authorization;

import Java.ru.geekbrains.NetworkChat.Exception.LoginNonExistent;
import Java.ru.geekbrains.NetworkChat.User;


public interface AuthService {
    boolean authUser(User user) throws LoginNonExistent;
    void registrationUser(User user) ;

}
