package qqserver.service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 该类用于管理和客户端通信的线程
 */
public class ManageClientThreads {
    private static final ConcurrentHashMap<String, ServerConnectClientThread> hm = new ConcurrentHashMap<>();

    // 添加线程对象到集合
    public static void addClientThread(String account, ServerConnectClientThread thread) {
        hm.put(account, thread);
    }

    // 根据 account 返回 ServerConnectClientThread 线程
    public static ServerConnectClientThread getThread(String account) {
        return hm.get(account);
    }

}
