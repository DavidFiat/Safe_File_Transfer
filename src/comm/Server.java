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

            //Obtenemos la informacion del archivo (Contenido y SHA-256
            String json2 = br.readLine();
            EncryptedFile ef = gson.fromJson(json2, EncryptedFile.class);
            byte[] encryptedFileBytes = ef.getInfo();
            String clientSHA = ef.getSHA256();

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

            //Calculamos el SHA-256 del archivo que ciframos
            //Usamos el algoritmo SHA-1
            MessageDigest shaDigest = MessageDigest.getInstance("SHA-256");
            //SHA-1 checksum
            String shaChecksum = getFileChecksum(shaDigest, new File(recievedPath));
            System.out.println("SHA-256: "+shaChecksum);

            //Comprobamos si el SHA-256 enviado por el cliente y el calculado son iguales
            if(clientSHA.equals(shaChecksum)){
                System.out.println("El archivo fue transferido adecuadamente");
            }


        } catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            e.printStackTrace();
        }
    }
    private static String getFileChecksum(MessageDigest digest, File file) throws IOException
    {
        //Get file input stream for reading the file content
        FileInputStream fis = new FileInputStream(file);

        //Create byte array to read data in chunks
        byte[] byteArray = new byte[1024];
        int bytesCount = 0;

        //Read file data and update in message digest
        while ((bytesCount = fis.read(byteArray)) != -1) {
            digest.update(byteArray, 0, bytesCount);
        };

        //close the stream; We don't need it now.
        fis.close();

        //Get the hash's bytes
        byte[] bytes = digest.digest();

        //This bytes[] has bytes in decimal format;
        //Convert it to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for(int i=0; i< bytes.length ;i++)
        {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        //return complete hash
        return sb.toString();
    }
}
