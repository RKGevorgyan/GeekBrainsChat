import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class MockAuthServiceImpl implements AuthService {

    private static MockAuthServiceImpl instance;
    //private HashMap<String, String> userDao;
    private static String currentUser;

    private MockAuthServiceImpl() {
       // userDao = new HashMap<>();
        //userDao.put("user", "pass");
        //currentUser = "user";
    }

    public static MockAuthServiceImpl getInstance() {
        if (instance == null) {
            instance = new MockAuthServiceImpl();
        }
        return instance;
    }

    public static String getCurrentUser() {
        return currentUser;
    }

    @Override
    public void addUser(String name, String pass) {
       // userDao.put(name, pass);
        DataBaseConnection.writeToDataBase(name,pass);
        currentUser = name;
    }

    @Override
    public boolean auth(String name, String pass) {
        if(name.equals(""))
            return false;
        String login = DataBaseConnection.readFromDataBase(name, pass);
        if (login != null) {
            currentUser = name;
            return true;
        }
        return false;
    }

    public void changeLogin(String login){
        if (!login.equals("")) {
            DataBaseConnection.updateLogin(login, currentUser);
            currentUser = login;
        }
    }

}
