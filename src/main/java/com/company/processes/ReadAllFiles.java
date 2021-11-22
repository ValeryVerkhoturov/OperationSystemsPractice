package com.company.processes;

import com.company.jsonWork.FileType;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.stream.IntStream;

/** Process 2 deamon */
public class ReadAllFiles implements Runnable {

    public void run() {
        for (FileType fileType: FileType.values()) {
            try {
                Thread thread = new Thread(new BytesReader(fileType, SocketChannel.open(new InetSocketAddress("localhost", 5454))));
                thread.run();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
