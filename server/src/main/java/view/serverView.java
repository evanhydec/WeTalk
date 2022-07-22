package view;

import entity.message;
import entity.messageType;
import entity.user;
import server.session;
import server.userServer;
import Thread.ThreadManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

@SuppressWarnings("all")
public class serverView {
    private ServerSocket server;

    public serverView() {
        System.out.println("服务端正在9999端口监听");
        try {
            ThreadManager manager = new ThreadManager();
            userServer instance = new userServer();
            server = new ServerSocket(9999);
            while (true) {
                Socket socket = server.accept();
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                message message = (message) ois.readObject();
                String[] info = message.getContent().split(" ");
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                message temp = new message();
                switch (message.getType()) {
                    case messageType.LOGIN:
                        user user = instance.exist(info[0], info[1]);
                        if (user != null) {
                            temp.setType(messageType.LOGIN_SUCCEED);
                            manager.add(socket, user);
                            System.out.println("登录: " + info[0]);
                            oos.writeObject(temp);
                        } else {
                            temp.setType(messageType.LOGIN_FAILED);
                            oos.writeObject(temp);
                            socket.close();
                        }
                        break;
                    case messageType.REGISTER:
                        if (instance.register(info[0], info[1])) {
                            temp.setType(messageType.REGISTER_SUCCEED);
                            System.out.println("注册: " + info[0]);
                        } else {
                            temp.setType(messageType.REGISTER_FAILED);
                        }
                        oos.writeObject(temp);
                        socket.close();
                        break;
                    default:
                        temp.setType(messageType.ERROR);
                        oos.writeObject(temp);
                        socket.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                session.shutdown();
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
