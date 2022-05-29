package comm;

import com.google.gson.Gson;
import model.EncryptedFile;
import model.Key;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
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

            OutputStream os = socket.getOutputStream();
            InputStream is = socket.getInputStream();

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
            BufferedReader br = new BufferedReader(new InputStreamReader(is));


            System.out.println("Conectado");

            //Con esto generamos el par de claves RSA
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(1024);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            //El par de claves RSA
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();


            //Mandamos la clave publica al cliente
            Gson gson = new Gson();
            Key key = new Key(publicKey.getEncoded());
            String json = gson.toJson(key);
            bw.write(json+"\n");
            bw.flush();

            //Obtenemos la informacion del archivo
            String json2 = br.readLine();
            EncryptedFile ef = gson.fromJson(json2, EncryptedFile.class);
            byte[] encryptedFileBytes = ef.getInfo();


            //Usamos Cipher para descifrar el archivo
            Cipher decryptCipher = Cipher.getInstance("RSA");
            decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedFileBytes = decryptCipher.doFinal(encryptedFileBytes);

            //Guardamos el archivo recibido
            String recievedPath = "DataReceived\\DecryptedFile";
            try (FileOutputStream fos = new FileOutputStream(recievedPath)) {
                fos.write(decryptedFileBytes);
                System.out.println("EXITO");
            }



        } catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            e.printStackTrace();
        }
    }
}
