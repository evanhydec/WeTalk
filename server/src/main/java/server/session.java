package server;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.*;

import java.io.IOException;
import java.io.InputStream;

public class session {
    private static SqlSession session;
    public static SqlSession getInstance() throws IOException {
        InputStream resource = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory build = new SqlSessionFactoryBuilder().build(resource);
        session = build.openSession(true);
        return session;
    }

    public static void shutdown() {
        session.close();
    }
}
