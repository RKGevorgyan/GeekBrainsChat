import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

@Component
public class ChatController implements Initializable {
    Logger log = Logger.getLogger(ChatController.class);

    public TextArea output;
    public TextField input;

    private ObjectOutputStream os;
    private Thread serverThread;

    public void send(ActionEvent actionEvent) throws IOException {
        os.writeObject(Message.of(MockAuthServiceImpl.getCurrentUser(), input.getText() + "\n"));
        input.clear();
    }

    public void quit(ActionEvent actionEvent) throws IOException {
        Parent chat = FXMLLoader.load(getClass().getResource("auth.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Регистрация");
        stage.setScene(new Scene(chat));
        stage.setResizable(false);
        stage.show();
        input.getScene().getWindow().hide();
        os.close();
        serverThread.interrupt();
        Platform.exit();
    }

    public void changeLogin(ActionEvent actionEvent) throws IOException {
        Parent chat = FXMLLoader.load(getClass().getResource("changeLogin.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Смена Логина");
        stage.setScene(new Scene(chat));
        stage.setResizable(false);
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Socket socket = null;
        try {
            socket = new Socket("localhost", 1234);
            os = new ObjectOutputStream(socket.getOutputStream());
            serverThread = new Thread(new ServerListener(socket, output));
            serverThread.start();
        } catch (IOException e) {
            //System.out.println("WARNING: Can't establish connection");
            log.error("WARNING: Can't establish connection");
        }
    }
}
