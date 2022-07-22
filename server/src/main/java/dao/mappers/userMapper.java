package dao.mappers;

import entity.user;
import org.apache.ibatis.annotations.Param;

public interface userMapper {
    user selectUser(@Param("user")user user);
    Integer insertUser(@Param("user")user user);
    int updateUser(@Param("name") String name, @Param("pwd")String password,@Param("or_name")String or_name);
    int delUser(@Param("id") Integer id);
}
