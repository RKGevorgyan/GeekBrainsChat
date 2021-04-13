import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ChatApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        ApplicationContext context = new AnnotationConfigApplicationContext(ClientConfig.class);
        ChatApplication application = context.getBean("chatApplication",ChatApplication.class);
        application.init(primaryStage);
    }

    public void init(Stage primaryStage) throws IOException {
        Parent auth = FXMLLoader.load(getClass().getResource("auth.fxml"));
        primaryStage.setTitle("Авторизация");
        primaryStage.setScene(new Scene(auth));
        primaryStage.show();
    }
}
