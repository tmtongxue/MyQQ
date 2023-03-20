package qqclient.service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 该类管理客户端连接服务器端的线程
 */
public class ManageClientThread {
    private static final ConcurrentHashMap<String, ClientConnectServiceThread> hm = new ConcurrentHashMap<>();

    public static void addThread(String account, ClientConnectServiceThread ccst) {
        hm.put(account, ccst);
    }

    public static ClientConnectServiceThread getThread(String account) {
        return hm.get(account);
    }
}
