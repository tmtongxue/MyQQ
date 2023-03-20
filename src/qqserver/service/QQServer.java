package qqserver.service;

import qqcommon.dao.UserInfoManager;
import qqcommon.output.Message;
import qqcommon.output.MessageType;
import qqcommon.output.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 监听9999，等待客户端连接，并保持通信
 */
public class QQServer {
    private ServerSocket serverSocket;

    public QQServer() {
        try {
            System.out.println("服务器已启动，等待客户端连接...");
            serverSocket = new ServerSocket(9999);

            while (true) {
                Socket socket = serverSocket.accept();
                // 得到socket关联的对象输入流
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                // 创建socket关联的对象输出流
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                // 读取客户端发送的User对象
                User user = (User) ois.readObject();
                // 创建Message对象准备回复客户端
                Message mes = new Message();
                // 验证
                UserInfoManager uim = new UserInfoManager();
                ConcurrentHashMap<String, String> account_password = uim.selectUser(1, 2); // 获取注册用户信息（账号-密码）
                ConcurrentHashMap<String, String> account_name_online = uim.selectOnlineUser(); // 获取在线用户信息（账号-昵称）
                ConcurrentHashMap<String, String> account_securityAnswer = uim.selectUser(1, 4); // 获取注册用户信息(账号-密保问题)
                String account = user.getAccount();
                String password = user.getPassword();
                String modifiedPassword = user.getModifiedPassword();
                String userName = user.getUserName();
                String securityAnswer = user.getSecurityAnswer();
                // 登录
                if (securityAnswer == null) {
                    if (account_password.containsKey(account) && account_password.get(account).equals(password)) { // 能够登录
                        if (account_name_online.containsKey(account)) { // 登录失败
                            System.out.println("用户" + user.getAccount() + "登录失败");
                            mes.setMesType(MessageType.MESSAGE_LOGIN_FAIL);
                            oos.writeObject(mes);
                            // 关闭socket
                            socket.close();
                        } else { // 登录成功
                            mes.setMesType(MessageType.MESSAGE_LOGIN_SUCCEED);
                            oos.writeObject(mes);
                            // 创建与客户端保持通信的线程
                            ServerConnectClientThread scct = new ServerConnectClientThread(socket, user.getAccount());
                            scct.start();
                            // 线程放入集合
                            ManageClientThreads.addClientThread(user.getAccount(), scct);
                        }
                    } else { // 登录失败
                        System.out.println("用户" + user.getAccount() + "登录失败");
                        mes.setMesType(MessageType.MESSAGE_LOGIN_FAIL);
                        oos.writeObject(mes);
                        // 关闭socket
                        socket.close();
                    }
                }

                // 注册
                if (account == null) {
                    if (userName.equals("") || password.equals("") || Objects.equals(securityAnswer, "")) {
                        System.out.println("用户" + user.getAccount() + "注册失败");
                        mes.setMesType(MessageType.MESSAGE_REGISTER_FAIL);
                        oos.writeObject(mes);
                        // 关闭socket
                        socket.close();
                    } else {
                        System.out.println("用户" + user.getUserName() + "注册成功");
                        mes.setMesType(MessageType.MESSAGE_REGISTER_SUCCEED);
                        oos.writeObject(mes);
                    }
                }

                // 找回密码
                if (password == null && modifiedPassword == null) {
                    if (account_securityAnswer.containsKey(account) && account_securityAnswer.get(account).equals(securityAnswer)) {
                        System.out.println("用户" + user.getAccount() + "找回密码成功");
                        mes.setMesType(MessageType.MESSAGE_FINDPWD_SUCCEED);
                        oos.writeObject(mes);
                    } else {
                        System.out.println("用户" + user.getAccount() + "找回密码失败");
                        mes.setMesType(MessageType.MESSAGE_FINDPWD_FAIL);
                        oos.writeObject(mes);
                        // 关闭socket
                        socket.close();
                    }
                }

                // 修改密码
                if (securityAnswer != null) {
                    if (Objects.equals(securityAnswer, "null") && modifiedPassword != null) {
                        if (account_password.containsKey(account) && account_password.get(account).equals(password)) {
                            System.out.println("用户" + user.getAccount() + "修改密码成功");
                            mes.setMesType(MessageType.MESSAGE_MODIFYPWD_SUCCEED);
                            oos.writeObject(mes);
                            uim.modifyPassword(modifiedPassword, account);
                        } else {
                            System.out.println("用户" + user.getAccount() + "修改密码失败");
                            mes.setMesType(MessageType.MESSAGE_MODIFYPWD_FAIL);
                            oos.writeObject(mes);
                            // 关闭socket
                            socket.close();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 如果服务端退出循环，关闭资源
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
