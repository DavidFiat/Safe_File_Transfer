package model;

import java.security.PublicKey;

public class Key {

    private byte[] publickeybytes;
    private PublicKey publicKey;

    public Key() {
    }

    public Key(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public Key(byte[] publickeybytes) {
        this.publickeybytes = publickeybytes;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public byte[] getPublickeybytes() {
        return publickeybytes;
    }

    public void setPublickeybytes(byte[] publickeybytes) {
        this.publickeybytes = publickeybytes;
    }
}
