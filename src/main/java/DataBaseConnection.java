import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

@Component
@Scope
public class DataBaseConnection {
    public static Connection getConnection() throws SQLException, IOException {
        Properties props = new Properties();
        String separator = File.separator;
        String path = "src/main/resources/connection.properties";
        InputStream in = new FileInputStream(path);
        props.load(in);
        String url = props.getProperty("url");
        String user = props.getProperty("user");
        String password = props.getProperty("password");
        return DriverManager.getConnection(url,user,password);
    }

    public static void writeToDataBase(String name, String pass){
        try(Connection connection = getConnection()) {
            Statement statement = connection.createStatement();
            statement.execute("Insert into Chat.user (login, password) values ('"+name+"','"+pass+"')");
        } catch (SQLException | IOException exception) {
            exception.printStackTrace();
        }
    }
    public static String readFromDataBase(String name, String pass){
        String query = "Select login, password from chat.user " +
                "where login = '"+name+"' and password = '"+pass+"'";
        try(Connection connection = getConnection()){
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                name = resultSet.getString("login");
            }
        } catch (SQLException | IOException e){
            e.printStackTrace();
            System.out.println("Problems with connection");
        }
        return name;
    }

    public static boolean updateLogin(String newLogin, String oldLogin){
        boolean flag=false;
        String query = "Update Chat.user set login = '"+newLogin+"' where login = '" + oldLogin+"'";
        try(Connection connection = getConnection()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            flag = true;
        } catch (SQLException | IOException exception) {
            exception.printStackTrace();
        }
        return flag;
    }
}

