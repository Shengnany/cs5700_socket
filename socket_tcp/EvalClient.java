import java.io.*;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;



public class EvalClient {
    public static final int SERVER_PORT = 8080;
    private static String[] expressions = {"1+1","3+4","99-10","36503-22"};
    public static void main(String... args)  {
        try {
            InetAddress IP = InetAddress.getLocalHost();
            System.out.println("LocalHost: " + IP );
            Socket clientSocket = new Socket( IP, SERVER_PORT);
            OutputStream os = clientSocket.getOutputStream();
            InputStream in = clientSocket.getInputStream();
            int n = expressions.length;
            byte[] byte2 = toBytes(n);
            os.write(byte2);
            for(int i=0; i<n;i++){
                byte2 = toBytes(expressions[i].length());
                os.write(byte2);
                byte[] exp = expressions[i].getBytes();
                int len = exp.length;
                for(;len>0;) {
                    byte[] bytes16 = new byte[16];
                    if (len <= 16) {
                        for (int j = 0; j < len; j++) {
                            bytes16[j] = exp[j];
                        }
                        os.write(bytes16, 0, len);
                        break;
                    } else {
                        for (int j = 0; j < 16; j++) {
                            bytes16[j] = exp[j];
                        }
                        len-=16;
                        os.write(bytes16, 0, 16);
                    }
                }
            }
            byte[] bytes = new byte[2];
            in.read(bytes, 0, 2);
            int numOfExpress = new BigInteger(bytes).intValue();
            for (int i = 0; i < numOfExpress; i++) {
                in.read(bytes, 0, 2);
                int len = new BigInteger(bytes).intValue();
                byte[] bytes16 = new byte[16];
                in.read(bytes16,0,len);
                System.out.println("Result"+i+": "+new String(bytes16,0,len)+";");

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
        // 4 -> 00000000 00000100
        // 4 >> 8 -> 00000000 0000000
}

}