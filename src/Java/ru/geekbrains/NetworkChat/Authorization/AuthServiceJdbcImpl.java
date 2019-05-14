package Java.ru.geekbrains.NetworkChat.Authorization;

import Java.ru.geekbrains.NetworkChat.Exception.LoginNonExistent;
import Java.ru.geekbrains.NetworkChat.Persistance.UserRepository;
import Java.ru.geekbrains.NetworkChat.User;

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
    public void registrationUser(User user)  {
        String login = user.getLogin();
        String password=user.getPassword();
        userRepository.insert(login,password);
     /*   if (userRepository.findByLogin(login)==null){ //имени не должно быть в базе
               userRepository.insert(login, password);
                    return true;
                }else{
                    throw new RegLoginException();
                }*/
        }


}
