package comm;

import com.google.gson.Gson;
import model.EncryptedFile;
import model.Key;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
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
        System.out.println("Ingresa el nombre del archivo a enviar por favor:");

            //Pedir el name para el path del archivo
            String name = scanner.nextLine();
            String path = "DataToSend\\"+name;
            File file = new File(path);
            FileWriter fw = new FileWriter(file);

            //Pedir el contenido del archivo
            System.out.println("Escribe el contenido:");
            String content = scanner.nextLine();
            fw.write(content);
            fw.close();

            //Empezando la conexion TCP
            System.out.println("Enviando solicitud...");

            //Socket es la puerta de conexion o comunicacion
            Socket socket = new Socket("127.0.0.1", 5000);


            System.out.println("Conectados");

            OutputStream os = socket.getOutputStream();
            InputStream is = socket.getInputStream();

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
            BufferedReader br = new BufferedReader(new InputStreamReader(is));


            //Recibimos la clave publica
            String json = br.readLine();
            Gson gson = new Gson();
            Key key = gson.fromJson(json, Key.class);
            byte[] publicKeyBytes = key.getPublickeybytes();


            System.out.println("Lo recibo");

            //Recuperamos la instancia de la clave publica
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

            //Ciframos la informacion del archivo
            byte[] fileBytes = Files.readAllBytes(Paths.get(path));
            System.out.println(fileBytes.length);
            Cipher encryptCipher = Cipher.getInstance("RSA");
            encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encryptedFileBytes = encryptCipher.doFinal(fileBytes);

            //Mandamos el archivo cifrado
            EncryptedFile ef = new EncryptedFile(encryptedFileBytes);
            String json2 = gson.toJson(ef);
            bw.write(json2+"\n");
            bw.flush();


        } catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException | InvalidKeySpecException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
