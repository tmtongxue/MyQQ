package qqclient.service;

import qqcommon.output.Message;
import qqcommon.output.MessageType;
import qqcommon.output.User;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class UserClientService {

    // 根据账号密码到服务器验证该用户是否合法
    public boolean checkUser(User u) {
        boolean flag = false;
        // 创建User对象
        try {
            Socket socket = new Socket(InetAddress.getByName("127.0.0.1"), 9999);
            // 得到ObjectOutputStream对象
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(u);

            // 读取从服务器回复的Message对象
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message mes = (Message) ois.readObject();

            switch (mes.getMesType()) {
                case MessageType.MESSAGE_LOGIN_SUCCEED -> {  // 登录成功
                    // 创建与服务端保持通信的线程
                    ClientConnectServiceThread ccst = new ClientConnectServiceThread(socket);
                    ccst.start();
                    // 将线程放入到集合管理
                    ManageClientThread.addThread(u.getAccount(), ccst);
                    flag = true;
                }
                case MessageType.MESSAGE_REGISTER_SUCCEED ->  // 注册成功
                        flag = true;
                case MessageType.MESSAGE_FINDPWD_SUCCEED ->  // 找回密码成功
                        flag = true;
                case MessageType.MESSAGE_MODIFYPWD_SUCCEED ->  // 修改密码成功
                        flag = true;
                case MessageType.MESSAGE_LOGIN_FAIL, MessageType.MESSAGE_REGISTER_FAIL, MessageType.MESSAGE_FINDPWD_FAIL, MessageType.MESSAGE_MODIFYPWD_FAIL ->
                        socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }
}
