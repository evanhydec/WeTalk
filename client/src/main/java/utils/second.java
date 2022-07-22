package utils;

import Threads.clientThread;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import entity.message;
import entity.messageType;
import entity.user;

import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class second {
    Socket socket;
    public static user user;

    public second(user user, Socket socket) {
        this.user = user;
        this.socket = socket;
    }


    public void onlineUser() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            message msg = new message();
            msg.setType(messageType.LIST_ONLINE);
            msg.setSend(user.getName());
            oos.writeObject(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        try {
            clientThread.shutdown();
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            message msg = new message(messageType.SHUT_DOWN);
            msg.setSend(user.getName());
            oos.writeObject(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void groupchat(String content) {
        message msg = new message();
        msg.setContent(content);
        msg.setType(messageType.GROUP);
        msg.setSend(user.getName());
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void chat(String id, String content) {
        try {
            message msg = new message();
            msg.setRcv(id);
            msg.setSend(user.getName());
            msg.setContent(content);
            msg.setType(messageType.CHAT);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void filesend(String id, String src) {
        try {
            File file = new File(src);
            BufferedInputStream bi = new BufferedInputStream(new FileInputStream(file));
            byte[] b = new byte[(int) file.length()];
            bi.read(b);
            message msg = new message();
            msg.setContent(file.getName());
            msg.setSend(user.getName());
            msg.setRcv(id);
            msg.setType(messageType.FILE);
            msg.setFile(b);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void rename(String name) {
        message message = new message(messageType.RENAME);
        message.setContent(name);
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changePwd(String pwd) {
        message message = new message(messageType.CHANGE_PWD);
        message.setContent(pwd);
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void destroy() {
        message message = new message(messageType.DESTROY);
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
