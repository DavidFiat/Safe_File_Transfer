package model;

public class EncryptedFile {

    private byte[] info;

    public EncryptedFile(byte[] info) {
        this.info = info;
    }

    public byte[] getInfo() {
        return info;
    }

    public void setInfo(byte[] info) {
        this.info = info;
    }
}
