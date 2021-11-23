package com.company.processes;

import com.company.jsonWork.FileType;
import lombok.SneakyThrows;
import lombok.Value;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/** Process 1 */
@Value
public class DataBaseWriter implements Runnable{

    FileType fileType;

    String hostname;

    int port;

    @SneakyThrows
    @Override
    public void run() {
        ServerSocket serverSocket = new ServerSocket(port, 10, InetAddress.getByName(hostname));
        Socket socket = serverSocket.accept();

        BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
        DataInputStream dis = new DataInputStream(bis);

        long fileLength = dis.readLong();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedOutputStream bos = new BufferedOutputStream(baos);
        for (int i = 0; i < fileLength; i++) bos.write(bis.read());
        System.out.println(fileType);
        bos.close();
        dis.close();
    }
}
