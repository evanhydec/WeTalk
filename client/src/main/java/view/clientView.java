package view;

import entity.user;
import utils.first;
import utils.second;

import java.net.Socket;
import java.util.Scanner;

public class clientView {
    private static boolean loop = true;
    private static Object lock = new Object();

    public static void stop(boolean flag) {
        synchronized (lock) {
            loop = flag;
            lock.notify();
        }
    }

    public clientView() {
        int choice = 0;
        Scanner scanner = new Scanner(System.in);
        first first = new first();
        do {
            System.out.println("=========欢迎登录网络通信系统=========");
            System.out.println("\t\t 1 登录系统");
            System.out.println("\t\t 2 注册账号");
            System.out.println("\t\t 9 退出系统");
            System.out.print("请输入你的选择：");
            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    System.out.print("请输入用户名:");
                    String name = scanner.next();
                    System.out.print("请输入密  码:");
                    String pwd = scanner.next();
                    try {
                        Socket socket = first.check(name, pwd);
                        if (socket != null) {
                            user user = new user(name, pwd);
                            second(user, socket);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    System.out.print("请输入用户名:");
                    name = scanner.next();
                    System.out.print("请输入密  码:");
                    pwd = scanner.next();
                    try {
                        first.sign(name, pwd);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 9:
                    System.out.println("退出系统");
                    break;
                default:
                    System.out.println("选择有误请重新输入！！");
                    break;
            }
        } while (choice != 9);
    }

    private void second(user user, Socket socket) {
        second second = null;
        try {
            second = new second(user, socket);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(System.in);

        do {
            System.out.println("=========网络通信系统二级菜单(用户: " + user.getName() + ")=========");
            System.out.println("\t\t 1 显示在线用户列表");
            System.out.println("\t\t 2 群发消息");
            System.out.println("\t\t 3 私聊消息");
            System.out.println("\t\t 4 发送文件");
            System.out.println("\t\t 5 修改用户名");
            System.out.println("\t\t 6 修改密码");
            System.out.println("\t\t 8 注销账户");
            System.out.println("\t\t 9 退出系统");
            System.out.println("请输入你的选择：");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    second.onlineUser();
                    break;
                case 2:
                    System.out.println("输入消息内容:");
                    String content = scanner.next();
                    second.groupchat(content);
                    break;
                case 3:
                    System.out.println("对方用户名:");
                    String id = scanner.next();
                    System.out.println("输入消息内容:");
                    content = scanner.next();
                    second.chat(id, content);
                    break;
                case 4:
                    System.out.println("对方用户名:");
                    id = scanner.next();
                    System.out.println("输入文件名:");
                    String src = scanner.next();
                    second.filesend(id, src);
                    break;
                case 5:
                    System.out.println("新用户名:");
                    id = scanner.next();
                    second.rename(id);
                    break;
                case 6:
                    System.out.println("新密码:");
                    String pwd = scanner.next();
                    second.changePwd(pwd);
                    break;
                case 8:
                    System.out.println("是否确认注销账户？(Y/N)");
                    String check = scanner.next();
                    if (check.equals("y") || check.equals("Y")) {
                        second.destroy();
                    }
                    break;
                case 9:
                    second.shutdown();
                    break;
                default:
                    System.out.println("选择有误请重新输入！！");
                    break;
            }
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {}
            }
        } while (loop);
    }
}
