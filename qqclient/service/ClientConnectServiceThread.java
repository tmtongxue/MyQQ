package qqclient.service;

import qqclient.clientUI.ChatUI;
import qqclient.clientUI.FriendsListUI;
import qqcommon.output.Message;
import qqcommon.output.MessageType;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ClientConnectServiceThread extends Thread {
    private final Socket socket;

    public ClientConnectServiceThread(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    public static ChatUI chatUI;

    @Override
    public void run() {
        while (true) {
            System.out.println("客户端线程，等待服务器发送的消息...");
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message mes = (Message) ois.readObject();
                // 判断 mes 类型，然后做相应的业务处理
                if (mes.getMesType().equals(MessageType.MESSAGE_COMM_MES)) { // 普通聊天消息
                    if (!FriendsListUI.isReceiveMes) {
                        FriendsListUI.messageAlert(mes.getSender());
                    }
                    if (FriendsListUI.isReceiveMes) {
                        if (!ChatUI.isOn) {
                            chatUI = new ChatUI(mes.getContent(), mes.getSender(), mes.getGetter(), mes.getSendTime());
                        } else {
                            chatUI.setContent(mes.getContent());
                            chatUI.setSender_acc(mes.getSender());
                            chatUI.setGetter_acc(mes.getGetter());
                            chatUI.setSendTime(mes.getSendTime());
                        }
                        chatUI.receiveMessage();
                    }
                }
                if (mes.getMesType().equals(MessageType.MESSAGE_FILE_MES)) { // 文件
                    FriendsListUI.fileAlert(mes.getSender());
                    if (FriendsListUI.isReceiveFile) {
                        System.out.println("成功接收文件" + mes.getFileName());
                        byte[] bytes = mes.getBytes();
                        // 将得到的bytes数组写入指定的路径，就得到一个文件
                        String destFilePath = "D:\\JavaQQData\\QQFileSaver\\" + mes.getFileName();
                        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destFilePath));
                        bos.write(bytes);
                        bos.close();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
