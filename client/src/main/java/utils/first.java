package utils;

import Threads.clientThread;
import entity.message;
import entity.messageType;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class first {

    public Socket check(String name,String pwd) throws Exception {
        Socket socket = new Socket("192.168.2.105", 9999);
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        message message = new message(messageType.LOGIN);
        message.setContent(name + " " + pwd);
        oos.writeObject(message);

        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        message feedback = (message) ois.readObject();
        if(feedback.getType() == messageType.LOGIN_SUCCEED){
            System.out.println("=========" + name + "登录成功" + "=========");
            new clientThread(socket).start();
            return socket;
        }else{
            System.out.println("=========登录失败，请检查输入=========");
            return null;
        }
    }

    public boolean sign(String name,String pwd) throws Exception {
        Socket socket = new Socket(InetAddress.getLocalHost(), 9999);
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        message message = new message(messageType.REGISTER);
        message.setContent(name + " " + pwd);
        oos.writeObject(message);

        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        message feedback = (message) ois.readObject();
        if (feedback.getType() == messageType.REGISTER_SUCCEED) {
            System.out.println("=========" + name + "注册成功" + "=========");
            return true;
        } else {
            System.out.println("=========" + name + "已存在，请重试" + "=========");
            return false;
        }
    }
}
