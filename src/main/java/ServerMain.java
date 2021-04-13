import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ServerMain {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ClientConfig.class);
        context.getBean("echoServer",EchoServer.class).startServer();
    }
}
