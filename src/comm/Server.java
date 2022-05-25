package comm;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.*;

public class Server {

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        try {

            ServerSocket server = new ServerSocket(5000);

            System.out.println("Esperando conexion");
            Socket socket = server.accept();
            System.out.println("Conectado");

            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(1024);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            try (FileOutputStream fos = new FileOutputStream("publicKey")) {
                fos.write(publicKey.getEncoded());
            }

            PrintWriter out = new PrintWriter(socket.getOutputStream());
            out.println(publicKey.toString());
            out.flush();

//            OutputStream os = socket.getOutputStream();
//
//
//            byte[] buffer = new byte[128];
//            int readBytes = 0;
//
//
//            while((readBytes = fis.read(buffer)) != -1) {
//                System.out.println(readBytes);
//                os.write(buffer, 0, readBytes);
//
//            }
//            os.close();

            System.out.println("Publica: "+ publicKey);
            System.out.println("Privada: "+ privateKey);

//            while(true) {}

        } catch (IOException | NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
