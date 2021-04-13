import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@PropertySource("classpath:server_port.properties")
public class EchoServer {

    Logger log = Logger.getLogger(EchoServer.class);

    @Value("${PORT}")
    private int PORT;

    private boolean running;
    private ConcurrentLinkedDeque<ClientHandler> clients = new ConcurrentLinkedDeque<>();
    private int counter = 0;
    private ClientHandler handler;

    @Autowired
    public void setHandler(ClientHandler handler) {
        this.handler = handler;
    }

    public void startServer(){
        running = true;
        try(ServerSocket serverSocket = new ServerSocket(PORT)){
            //System.out.println("Server started!");
            log.info("Server started");
            while (running){
                //System.out.println("Server is waiting connection");
                log.info("Server is waiting connection");
                Socket socket = serverSocket.accept();
                //System.out.println("Client accepted!");
                log.info("Client accepted");
                counter++;
               // ClientHandler handler = new ClientHandler(socket, this);
                clients.add(handler);
                ExecutorService executor = Executors.newCachedThreadPool();
                Object mon = new Object();
                executor.execute(handler);
//                new Thread(handler).start();
                //System.out.println("Connected user: " + counter);
                log.info("Connected user: " + counter);
                executor.shutdownNow();
            }
        } catch (IOException e) {
            //System.out.println("Server was broken");
            log.error("Server was broken");
        }

    }

//    public boolean isRunning() {
//
//        return running;
//    }
//
//    public void setRunning(boolean running) {
//
//        this.running = running;
//    }

    public void broadCast(Message message) throws IOException {
        for (ClientHandler client: clients) {
            client.sendMessage(message);
        }
    }

    public void kickClient(ClientHandler client){

        clients.remove(client);
    }

}