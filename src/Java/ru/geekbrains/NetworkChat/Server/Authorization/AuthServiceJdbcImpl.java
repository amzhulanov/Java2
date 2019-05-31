package Java.ru.geekbrains.NetworkChat.Server.Authorization;

import Java.ru.geekbrains.NetworkChat.Server.Exception.LoginNonExistent;
import Java.ru.geekbrains.NetworkChat.Server.Exception.RegLoginException;
import Java.ru.geekbrains.NetworkChat.Server.Persistance.UserRepository;
import Java.ru.geekbrains.NetworkChat.Server.User;

public class AuthServiceJdbcImpl implements AuthService {

    private UserRepository userRepository;

    public AuthServiceJdbcImpl(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @Override
    public boolean authUser(User user) throws LoginNonExistent {//авторизовываю пользователя через userRepository
        String login = user.getLogin();
        User searchUser = null;
        searchUser = userRepository.findByLogin(login);//проверяю наличие имени в базе
        if (searchUser==null){
            throw new LoginNonExistent();
        }
        String pwd = user.getPassword();
        return pwd != null && pwd.equals(searchUser.getPassword());
    }


    @Override
    public void registrationUser(User user) throws RegLoginException {
        if (!userRepository.insert(user)) {
            throw new RegLoginException();
        }

     /*   if (userRepository.findByLogin(login)==null){ //имени не должно быть в базе
               userRepository.insert(login, password);
                    return true;
                }else{
                    throw new RegLoginException();
                }*/
        }


}
