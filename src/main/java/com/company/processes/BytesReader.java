package com.company.processes;

import com.company.jsonWork.FileType;
import lombok.Value;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;

@Value
public class BytesReader implements Runnable{

    FileType fileType;

    SocketChannel client;

    public void run() {
        try {
//            client = SocketChannel.open(new InetSocketAddress("localhost", 5454));
            sendFile();
            client.close();

        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }


    public void sendFile() throws InterruptedException, IOException {
        ByteBuffer buffer = ByteBuffer.wrap(synchronizedBytesReading());
        client.write(buffer);
        buffer.clear();
    }

    private byte[] synchronizedBytesReading() {
        byte[] bytes = null;
        switch (fileType){
            case FIRST:
                synchronized (FileType.FIRST){
                    bytes = bytesReading();
                }
                break;
            case SECOND:
                synchronized (FileType.SECOND){
                    bytes = bytesReading();
                }
                break;
            case THIRD:
                synchronized (FileType.THIRD){
                    bytes = bytesReading();
                }
                break;
            default:
                try {
                    throw new FileNotFoundException("Unexpected file");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
        }
        return bytes;
    }

    private byte[] bytesReading(){
        File file = new File(fileType.getPath());
        if (!file.exists())
            return null;
        try {
            return Files.readAllBytes(Path.of(fileType.getPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
