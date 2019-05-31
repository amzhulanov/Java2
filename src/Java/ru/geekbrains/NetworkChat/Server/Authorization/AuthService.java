package Java.ru.geekbrains.NetworkChat.Server.Authorization;

import Java.ru.geekbrains.NetworkChat.Server.Exception.LoginNonExistent;
import Java.ru.geekbrains.NetworkChat.Server.Exception.RegLoginException;
import Java.ru.geekbrains.NetworkChat.Server.User;


public interface AuthService {
    boolean authUser(User user) throws LoginNonExistent;
    void registrationUser(User user) throws RegLoginException;

}
