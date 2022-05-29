package comm;

import com.google.gson.Gson;

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

            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();


            //Mandamos la clave publica al cliente

            Gson gson = new Gson();

            String json = gson.toJson(publicKey.getEncoded());
            //String json = gson.toJson(publicKey);
            bw.write(json+"\n");
            bw.flush();
//            DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
//            dOut.writeInt(json.length()); // write length of the message
//            dOut.write(publicKey.getEncoded());           // write the message


            //Obtenemos la informacion del archivo
            byte[] encryptedFileBytes = is.readAllBytes();

            //Usamos Cipher para descifrar el archivo
            Cipher decryptCipher = Cipher.getInstance("RSA");
            decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedFileBytes = decryptCipher.doFinal(encryptedFileBytes);

            String recievedPath = "data\\Message";
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
