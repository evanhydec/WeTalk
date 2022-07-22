package entity;

public interface messageType {
    int ERROR = 0;
    int LOGIN = 1;
    int REGISTER = 2;
    int LOGIN_SUCCEED = 3;
    int LOGIN_FAILED = 4;
    int REGISTER_SUCCEED = 5;
    int REGISTER_FAILED = 6;
    int LIST_ONLINE = 7;
    int SHUT_DOWN = 8;
    int CHAT = 9;
    int SYSTEM = 10;
    int GROUP = 11;
    int FILE = 12;
    int SYSTEM_R = 13;
    int RENAME = 14;
    int CHANGE_PWD = 15;
    int RENAME_SUCCEED = 16;
    int RENAME_FAILED = 17;
    int CHANGEPWD_SUCCEED = 18;
    int CHANGEPWD_FAILED = 19;
    int DESTROY = 20;
    int DESTROY_SUCCEED = 21;
    int DESTROY_FAILED = 22;
    int LOGOUT = 23;
}
