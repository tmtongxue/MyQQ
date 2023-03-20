package qqclient.service;

import qqcommon.output.Message;
import qqcommon.output.MessageType;
import utils.StreamUtils;

import java.io.*;
import java.net.Socket;

public class FileClientService {

    public void sendFileToOne(File file, String sender_acc, String getter_acc) {
        Message mes =  new Message();
        mes.setMesType(MessageType.MESSAGE_FILE_MES);
        mes.setSender(sender_acc);
        mes.setGetter(getter_acc);
        mes.setFileName(file.getName());
        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(file.getAbsolutePath()));
            mes.setBytes(StreamUtils.streamToByteArray(bis));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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
