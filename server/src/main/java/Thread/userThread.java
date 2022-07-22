package Thread;

import entity.message;
import entity.messageType;
import entity.user;
import server.userServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class userThread extends Thread {
    Socket socket;
    SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss E");
    user user;
    boolean loop = true;


    public userThread(Socket socket, user user) {
        this.socket = socket;
        this.user = user;
    }

    public void send(message message) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    @SuppressWarnings("all")
    public void run() {
        System.out.println(sdf.format(new Date()) + "  用户 " + user.getName() + " 登录客户端");
        try {
            userServer server = new userServer();
            while (loop) {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                message msg = (message) ois.readObject();
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                message fallback = new message(messageType.SYSTEM);
                switch (msg.getType()) {
                    case messageType.LIST_ONLINE:
                        System.out.println(sdf.format(new Date()) + "  " + msg.getSend() +
                                " 请求拉取在线用户列表");
                        fallback.setContent(ThreadManager.online());
                        fallback.setType(messageType.LIST_ONLINE);
                        break;
                    case messageType.SHUT_DOWN:
                        loop = false;
                        ThreadManager.del(msg.getSend());
                        System.out.println(sdf.format(new Date()) + "  用户 " + msg.getSend() + " 退出客户端");
                        fallback.setType(messageType.LOGOUT);
                        break;
                    case messageType.CHAT:
                        userThread target = ThreadManager.get(msg.getRcv());
                        if (target != null) {
                            target.send(msg);
                            fallback.setContent("内容发送成功");
                            System.out.println(sdf.format(new Date()) + "  " + msg.getSend() + " 发送私聊给" + msg.getRcv());
                        } else {
                            fallback.setContent("对方不在线！信息发送失败");
                        }
                        break;
                    case messageType.GROUP:
                        for (String user : ThreadManager.online().split(" ")) {
                            if (!user.equals(msg.getSend())) {
                                ThreadManager.get(user).send(msg);
                            }
                        }
                        fallback.setContent("内容发送成功");
                        System.out.println(sdf.format(new Date()) + "  " + msg.getSend() + " 发送群聊信息");
                        break;
                    case messageType.FILE:
                        target = ThreadManager.get(msg.getRcv());
                        if (target != null) {
                            target.send(msg);
                            fallback.setContent("文件发送成功");
                            System.out.println(sdf.format(new Date()) + "  " + msg.getSend() + " 发送文件给" + msg.getRcv());
                        } else {
                            fallback.setContent("对方不在线！文件发送失败");
                        }
                        break;
                    case messageType.RENAME:
                        if (server.rename(user.getName(), msg.getContent())) {
                            fallback.setType(messageType.RENAME_SUCCEED);
                            ThreadManager.update(user.getName(), msg.getContent());
                            user.setName(msg.getContent());
                            fallback.setContent(msg.getContent());
                            System.out.println(sdf.format(new Date()) + "  用户 " + user.getName() + " 更名为" + msg.getContent());
                        } else {
                            fallback.setType(messageType.RENAME_FAILED);
                        }
                        break;
                    case messageType.CHANGE_PWD:
                        if (server.changePwd(user.getName(), msg.getContent())) {
                            fallback.setType(messageType.CHANGEPWD_SUCCEED);
                            System.out.println(sdf.format(new Date()) + "  用户 " + user.getName() + " 修改密码");
                        } else {
                            fallback.setType(messageType.CHANGEPWD_FAILED);
                        }
                        break;
                    case messageType.DESTROY:
                        loop = false;
                        ThreadManager.del(msg.getSend());
                        server.destroy(user.getId());
                        System.out.println(sdf.format(new Date()) + "  用户 " + user.getName() + " 注销账户");
                        fallback.setType(messageType.DESTROY_SUCCEED);
                        break;
                    default:
                        System.out.println(sdf.format(new Date()) + "  " + "未解析类型");
                        fallback.setType(messageType.ERROR);
                }
                oos.writeObject(fallback);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
