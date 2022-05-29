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
import java.security.*;
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

            //Calculamos el SHA-256 del archivo que ciframos
            //Usamos el algoritmo SHA-1
            MessageDigest shaDigest = MessageDigest.getInstance("SHA-256");
            //SHA-1 checksum
            String shaChecksum = getFileChecksum(shaDigest, file);
            System.out.println("SHA-256: "+shaChecksum);

            //Mandamos el archivo cifrado junto con su hash SHA-256
            EncryptedFile ef = new EncryptedFile(encryptedFileBytes,shaChecksum);
            String json2 = gson.toJson(ef);
            bw.write(json2+"\n");
            bw.flush();

        } catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException | InvalidKeySpecException e) {
            // TODO Auto-generated catch block
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
