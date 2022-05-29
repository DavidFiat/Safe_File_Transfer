package comm;

import com.google.gson.Gson;

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
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Scanner;


public class Client {

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        try {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingresa el path del archivo a enviar por favor");
       // String path = scanner.nextLine();

            //Pedir el path del archivo
            String path = "data\\M.JPG";
            File file = new File(path);


            //Empezando la conexion TCP
            System.out.println("Enviando solicitud...");

            //Socket es la puerta de conexion o comunicacion
            //Para conectarme conmigo mismo
            Socket socket = new Socket("127.0.0.1", 5000);

            //Para conectarse con conmpañeros y profesor ngrok
            //Socket socket = new Socket("0.tcp.ngrok.io", 10828);

            System.out.println("Conectados");

            OutputStream os = socket.getOutputStream();
            InputStream is = socket.getInputStream();

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
            BufferedReader br = new BufferedReader(new InputStreamReader(is));


            System.out.println("HOLA");
            //Recibimos la clave publica
            //Necesitamos cambiar esto para leer solo los Bytes de la clave

            String json = br.readLine();
            Gson gson = new Gson();


            PublicKey publicKey = (PublicKey) gson.fromJson(json);

//            DataInputStream dIn = new DataInputStream(socket.getInputStream());
//            int length = dIn.readInt();                    // read length of incoming message
//            byte[] publicKeyBytes = new byte[length];
//            dIn.readFully(publicKeyBytes, 0, publicKeyBytes.length); // read the message

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
            os.write(encryptedFileBytes);


            is.close();
            stream.close();




        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
