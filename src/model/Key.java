package model;

/**
 * Key Class
 * @author Mateo Loaiza
 * @author David Fiat
 */
public class Key {

    private byte[] publicKeyBytes;

    /**
     * Empty constructor of the class Key
     */
    public Key() {
    }

    /**
     * Constructor with parameters of the class Key
     * @param publicKeyBytes array containing the bytes of the public key
     */
    public Key(byte[] publicKeyBytes) {
        this.publicKeyBytes = publicKeyBytes;
    }

    /**
     * This is the get method for the attribute publicKeyBytes
     * @return the array containing the bytes of the public key
     */
    public byte[] getPublicKeyBytes() {
        return publicKeyBytes;
    }

}
