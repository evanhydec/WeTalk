package Thread;

import entity.user;

import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ThreadManager {
    static Map<String, userThread> map = new HashMap<>();

    public static String online() {
        String res = "";
        Iterator<String> iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            res += iterator.next() + " ";
        }
        return res;
    }

    public static userThread get(String name) {
        return map.get(name);
    }

    public static void update(String or_name, String name) {
        map.put(name, map.get(or_name));
        map.remove(or_name);
    }

    public static void add(Socket socket, user user) {
        userThread userThread = new userThread(socket, user);
        userThread.start();
        map.put(user.getName(), userThread);
    }

    public static void del(String name) {
        map.remove(name);
    }
}
