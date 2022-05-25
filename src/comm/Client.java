package comm;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class Client {

    public static void main(String[] args) {
        // TODO Auto-generated method stub


        try {

            String path = "D:\\Documents\\Quinto Semestre\\ProRed\\Test\\Enviar\\java.pdf";
            File file = new File(path);
            String ab = file.getAbsolutePath();
            FileInputStream fis = new FileInputStream(file);

            System.out.println("Enviando solicitud...");

            //Socket es la puerta de conexion o comunicacion
            //Para conectarme conmigo mismo
            Socket socket = new Socket("127.0.0.1", 5000);

            //Para conectarse con conmpañeros y profesor ngrok
            //Socket socket = new Socket("0.tcp.ngrok.io", 10828);

            System.out.println("Conectados");

            File publicKeyFile = new File("publicKey");
            byte[] publicKeyBytes = Files.readAllBytes(publicKeyFile.toPath());

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

            byte[] fileBytes = Files.readAllBytes(Path.of(file.getPath()));

            Cipher encryptCipher = Cipher.getInstance("RSA");
            encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encryptedFileBytes = encryptCipher.doFinal(fileBytes);



//            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            String publicKey = in.readLine();
//            System.out.println("Publica: "+publicKey);



        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }
}
