package Threads;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import entity.message;
import entity.messageType;
import entity.user;
import jdk.nashorn.internal.runtime.ECMAException;
import utils.second;
import view.clientView;

import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class clientThread extends Thread {
    private static boolean loop = true;
    private Socket socket;

    public static void shutdown() {
        loop = false;
    }

    public clientThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    @SuppressWarnings("all")
    public void run() {
        try {
            while (loop) {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                message msg = (message) ois.readObject();
                boolean flag = true;
                switch (msg.getType()) {
                    case messageType.LIST_ONLINE:
                        System.out.println("=========在线用户列表=========");
                        String[] list = msg.getContent().split(" ");
                        for (String name : list) {
                            System.out.println("用户: " + name);
                        }
                        break;
                    case messageType.SYSTEM:
                        System.out.println("=========系统提示=========");
                        System.out.println(msg.getContent());
                        break;
                    case messageType.CHAT:
                        System.out.println("=========新的私聊信息=========");
                        System.out.println(msg.getSend() + " 对 你 说:");
                        System.out.println(msg.getContent());
                        break;
                    case messageType.GROUP:
                        System.out.println("=========新的群聊信息=========");
                        System.out.println(msg.getSend() + " 对 大家 说:");
                        System.out.println(msg.getContent());
                        break;
                    case messageType.FILE:
                        System.out.println("=========新的文件传输=========");
                        System.out.println(msg.getSend() + "发送 文件 给 你");
                        File desktopDir = FileSystemView.getFileSystemView() .getHomeDirectory();
                        String parent = desktopDir.getAbsolutePath();
                        File file = new File(parent, msg.getContent());
                        System.out.println("读取到文件");
                        BufferedOutputStream bw = new BufferedOutputStream(new FileOutputStream(file));
                        bw.write(msg.getFile());
                        bw.close();
                        System.out.println("下载完成，保存到" + parent + "\\" + msg.getContent());
                        break;
                    case messageType.SYSTEM_R:
                        System.out.println("=========新的系统推送=========");
                        System.out.println(msg.getContent());
                        break;
                    case messageType.RENAME_SUCCEED:
                        System.out.println("改名成功");
                        second.user.setName(msg.getContent());
                        break;
                    case messageType.RENAME_FAILED:
                        System.out.println("已存在该用户，请重新选择昵称");
                        break;
                    case messageType.CHANGEPWD_SUCCEED:
                        System.out.println("修改密码成功");
                        second.user.setPwd(msg.getContent());
                        break;
                    case messageType.CHANGEPWD_FAILED:
                        System.out.println("密码修改失败");
                        break;
                    case messageType.DESTROY_SUCCEED:
                        System.out.println("账户注销成功");
                        flag = false;
                        break;
                    case messageType.DESTROY_FAILED:
                        System.out.println("账户注销失败");
                        break;
                    case messageType.LOGOUT:
                        flag = false;
                        System.out.println("退出成功,将于三秒钟后退出");
                        TimeUnit.SECONDS.sleep(3);
                        break;
                    case messageType.ERROR:
                        System.out.println("错误选项");
                        break;
                    default:
                        System.out.println("未解析类型");
                }
                clientView.stop(flag);
            }
        } catch (Exception e) {}
    }
}
