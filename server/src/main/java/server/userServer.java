package server;

import dao.mappers.userMapper;
import entity.user;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;


public class userServer {
    private SqlSession sqlSession;
    private userMapper mapper;

    public userServer() throws IOException {
        sqlSession = session.getInstance();
        mapper = sqlSession.getMapper(userMapper.class);
    }

    public boolean register(String name, String pwd) {
        user res = mapper.selectUser(new user(name, pwd));
        if (res == null) {
            mapper.insertUser(new user(name, pwd));
            return true;
        } else {
            return false;
        }
    }

    public user exist(String name, String pwd) {
        user res = mapper.selectUser(new user(name, pwd));
        return res;
    }

    public void destroy(Integer id) {
        mapper.delUser(id);
    }

    public boolean rename(String orname, String name) {
        return mapper.updateUser(name, null, orname) == 1;
    }

    public boolean changePwd(String orname, String pwd) {
        return mapper.updateUser(null, pwd, orname) == 1;
    }


}
