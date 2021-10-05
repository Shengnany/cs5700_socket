
import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;

public class ThreadedEvalServer {
    public static final int SERVER_PORT = 8080;

    public static void main(String... args) throws IOException {
          ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            serverSocket.close();
        }
    }
}