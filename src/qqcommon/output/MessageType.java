package qqcommon.output;

public interface MessageType {
    String MESSAGE_LOGIN_SUCCEED = "1"; // 表示登录成功
    String MESSAGE_LOGIN_FAIL = "2"; // 表示登录失败
    String MESSAGE_REGISTER_SUCCEED = "3"; // 表示注册成功
    String MESSAGE_REGISTER_FAIL = "4"; // 表示注册失败
    String MESSAGE_FINDPWD_SUCCEED = "5"; // 表示找回密码成功
    String MESSAGE_FINDPWD_FAIL = "6"; // 表示找回密码失败
    String MESSAGE_MODIFYPWD_SUCCEED = "7"; // 表示修改密码成功
    String MESSAGE_MODIFYPWD_FAIL = "8"; // 表示修改密码失败
    String MESSAGE_COMM_MES = "9"; // 表示聊天消息
    String MESSAGE_FILE_MES = "10"; // 表示文件消息
}
