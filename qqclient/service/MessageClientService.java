package qqclient.service;

import qqcommon.output.Message;
import qqcommon.output.MessageType;

import java.io.ObjectOutputStream;
import java.net.Socket;

public class MessageClientService {

    public void sendMessageToOne(String content, String sender_acc, String getter_acc, String sendTime) {
        Message mes =  new Message();
        mes.setMesType(MessageType.MESSAGE_COMM_MES);
        mes.setContent(content);
        mes.setSender(sender_acc);
        mes.setGetter(getter_acc);
        mes.setSendTime(sendTime);

        // 发送给服务端
        try {
            Socket socket = ManageClientThread.getThread(sender_acc).getSocket();
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(mes);

        } catch (Exception e) {
           e.printStackTrace();
        }
    }
}
