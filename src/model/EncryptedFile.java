package model;

public class EncryptedFile {

    private byte[] info;
    private String SHA256;

    public EncryptedFile() {
    }

    public EncryptedFile(byte[] info, String SHA256) {
        this.info = info;
        this.SHA256 = SHA256;
    }

    public String getSHA256() {
        return SHA256;
    }

    public void setSHA256(String SHA256) {
        this.SHA256 = SHA256;
    }

    public byte[] getInfo() {
        return info;
    }

    public void setInfo(byte[] info) {
        this.info = info;
    }
}
