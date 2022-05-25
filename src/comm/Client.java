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

            //Pedir el path del archivo
            String path = "D:\\Documents\\Octavo Semestre\\Ciberseguridad\\Workspace\\Mensaje.txt";
            File file = new File(path);
//            String ab = file.getAbsolutePath();
//            FileInputStream fis = new FileInputStream(file);

            //Empezando la conexion TCP
            System.out.println("Enviando solicitud...");

            //Socket es la puerta de conexion o comunicacion
            //Para conectarme conmigo mismo
            Socket socket = new Socket("127.0.0.1", 5000);

            //Para conectarse con conmpañeros y profesor ngrok
            //Socket socket = new Socket("0.tcp.ngrok.io", 10828);

            System.out.println("Conectados");

            System.out.println("HOLA");
            //Recibimos la clave publica
            InputStream is = socket.getInputStream();
            //Necesitamos cambiar esto para leer solo los Bytes de la clave
            byte[] publicKeyBytes = is.readAllBytes();
            System.out.println("Lo recibo");

            //Recuperamos la instancia de la clave publica
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

            //Ciframos la informacion del archivo
            byte[] fileBytes = Files.readAllBytes(Path.of(file.getPath()));

            Cipher encryptCipher = Cipher.getInstance("RSA");
            encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encryptedFileBytes = encryptCipher.doFinal(fileBytes);

            //Mandamos el archivo cifrado
            FileOutputStream stream = new FileOutputStream("EncryptedFile");
            stream.write(encryptedFileBytes);
            OutputStream os = socket.getOutputStream();
            os.write(encryptedFileBytes);


            is.close();
            stream.close();




        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
