
import java.io.*;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class EvalServer {
    public static final int SERVER_PORT = 8080;

    public static void main(String... args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(SERVER_PORT);

        try {
            Socket clientSocket = serverSocket.accept();

            InputStream in = clientSocket.getInputStream();
            OutputStream out = clientSocket.getOutputStream();

            byte[] bytes = new byte[2];
            in.read(bytes, 0, 2);
            int numOfExpress = new BigInteger(bytes).intValue();

            out.write(toBytes(numOfExpress));
            for (int i = 0; i < numOfExpress; i++) {
                in.read(bytes, 0, 2);
                int len = new BigInteger(bytes).intValue();
                int sum = 0;
                int sign = 1;
                int num = 0;
                for (int j = 0; j < len; j++) {
                    int a = in.read();
                    if (a >= '0' && a <= '9') {
                        num = num * 10 + (a - '0');
                    } else if (a == '+') {
                        sum += sign * num;
                        sign = 1;
                        num = 0;
                    } else if (a == '-') {
                        sum += sign * num;
                        sign = -1;
                        num = 0;
                    }
                }
                sum += sign * num;
                String ans = String.valueOf(sum);
                out.write(toBytes(ans.length()));
                out.write(ans.getBytes());
            }
            clientSocket.close();


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            serverSocket.close();
        }
    }

    public static byte[] toBytes(int i) {
        byte[] result = new byte[2];
        result[0] = (byte) (i >> 8);
        result[1] = (byte) (i);
        return result;
        // 4 -> 00000000 00000100
        // 4 >> 8 -> 00000000 0000000
    }
}
