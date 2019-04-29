package Java.ru.geekbrains.NetworkChat.Authorization;

import Java.ru.geekbrains.NetworkChat.User;

import java.util.HashMap;
import java.util.Map;

public class AuthServiceImp implements AuthService {

        public Map<String,String> users=new HashMap<>();

    public AuthServiceImp() {
        users.put("ivan","123");
        users.put("koly","321");
        users.put("max","000");
    }

    @Override
    public boolean authUser(User user) {//проверка введённого пароля
        String pwd=users.get(user.getLogin());
        return pwd !=null&& pwd.equals(user.getPassword());
    }
}
