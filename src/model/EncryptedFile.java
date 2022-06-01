package model;

import java.io.Serializable;

/**
 * EncryptedFile Class
 * @author Mateo Loaiza
 * @author David Fiat
 */
public class EncryptedFile implements Serializable {

    private byte[] info;
    private String SHA256;

    /**
     * Empty constructor of the class EncryptedFile
     */
    public EncryptedFile() {
    }

    /**
     * Constructor with parameters of the class EncryptedFile
     * @param info byte array with the information of the file
     * @param SHA256 string with the calculated hash SHA-256
     */
    public EncryptedFile(byte[] info, String SHA256) {
        this.info = info;
        this.SHA256 = SHA256;
    }

    /**
     * This is the get method for the attribute SHA256
     * @return string with the calculated hash SHA-256
     */
    public String getSHA256() {
        return SHA256;
    }
    /**
     * This is the get method for the attribute info
     * @return byte array with the information of the file
     */
    public byte[] getInfo() {
        return info;
    }
}
