import java.io.*;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket clientSocket;


    private static String[] expressions = {"1+1","3+4","99-10","36503-22"};

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        String client = String.format("[%s:%d]", clientSocket.getInetAddress(), clientSocket.getPort());
        System.out.println(String.format("Handle client %s", client));
        try {
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
        }
    }

    public static byte[] toBytes(int i) {
        byte[] result = new byte[2];
        result[0] = (byte) (i >> 8);
        result[1] = (byte) (i);
        return result;
    }
}


