package entity;

import java.io.Serializable;
import java.util.Arrays;

public class message implements Serializable {
    private int type;
    private String rcv;
    private String send;
    private String content;
    private byte[] file;

    @Override
    public String toString() {
        return "message{" +
                "type=" + type +
                ", rcv='" + rcv + '\'' +
                ", send='" + send + '\'' +
                ", content='" + content + '\'' +
                ", file=" + Arrays.toString(file) +
                '}';
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public message(){}

    public message(int type) {
        this.type = type;
    }

    public message(int type, String rcv, String send, String content, byte[] file) {
        this.type = type;
        this.rcv = rcv;
        this.send = send;
        this.content = content;
        this.file = file;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getRcv() {
        return rcv;
    }

    public void setRcv(String rcv) {
        this.rcv = rcv;
    }

    public String getSend() {
        return send;
    }

    public void setSend(String send) {
        this.send = send;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
