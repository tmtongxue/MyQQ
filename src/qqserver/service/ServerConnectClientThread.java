package qqserver.service;

import qqcommon.dao.UserInfoManager;
import qqcommon.output.Message;
import qqcommon.output.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerConnectClientThread extends Thread {
    private final Socket socket;
    private final String account;
    private final UserInfoManager uim = new UserInfoManager();

    public ServerConnectClientThread(Socket socket, String account) {
        this.socket = socket;
        this.account = account;
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println("服务器和客户端" + account + "保持通信，读取数据...");
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message mes = (Message) ois.readObject();

                // 判断 mes 类型，然后做相应的业务处理
                if (mes.getMesType().equals(MessageType.MESSAGE_COMM_MES)) { // 普通消息
                    Socket socket = ManageClientThreads.getThread(mes.getGetter()).getSocket();
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(mes);
                }
                if (mes.getMesType().equals(MessageType.MESSAGE_FILE_MES)) { // 文件
                    Socket socket = ManageClientThreads.getThread(mes.getGetter()).getSocket();
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(mes);
                    System.out.println(account + "发送文件成功");
                }
            } catch (Exception e) {
                System.out.println(account + "退出");
                uim.deleteOnlineUser(account);
                break;
            }
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
