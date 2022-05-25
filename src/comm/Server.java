package comm;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.security.*;

public class Server {

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        try {

            ServerSocket server = new ServerSocket(5000);

            System.out.println("Esperando conexion");
            Socket socket = server.accept();
            System.out.println("Conectado");

            //Con esto generamos el par de claves RSA
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(1024);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();


            //Grabamos la clave pública a un archivo
            try (FileOutputStream fos = new FileOutputStream("publicKey")) {
                fos.write(publicKey.getEncoded());
            }
            //Grabamos la clave privada a un archivo
            try (FileOutputStream fos = new FileOutputStream("privateKey")) {
                fos.write(privateKey.getEncoded());
            }

            //Mandamos la clave publica al cliente
            OutputStream os = socket.getOutputStream();
            os.write(publicKey.getEncoded());
            System.out.println("Lo mando");
            os.flush();

            //Obtenemos la informacion del archivo
            InputStream is = socket.getInputStream();
            byte[] encryptedFileBytes = is.readAllBytes();

            //Usamos Cipher para descifrar el archivo
            Cipher decryptCipher = Cipher.getInstance("RSA");
            decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedFileBytes = decryptCipher.doFinal(encryptedFileBytes);

            String recievedPath = "D:\\Documents\\Octavo Semestre\\Recibidos\\Mensaje";
            try (FileOutputStream fos = new FileOutputStream(recievedPath)) {
                fos.write(decryptedFileBytes);
                System.out.println("EXITO");
            }

            os.close();
//            System.out.println("Publica: "+ publicKey);
//            System.out.println("Privada: "+ privateKey);

//            while(true) {}

        } catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
