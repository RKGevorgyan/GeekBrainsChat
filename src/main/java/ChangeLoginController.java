import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ChangeLoginController {
    @FXML
    TextField login;


    public void change(ActionEvent actionEvent) throws IOException {
        MockAuthServiceImpl.getInstance().changeLogin(login.getText());
        login.getScene().getWindow().hide();
    }
}
