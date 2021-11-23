package com.company.processes;

import com.company.jsonWork.FileType;
import lombok.SneakyThrows;
import lombok.Value;

import java.io.*;
import java.net.Socket;

@Value
public class FileReader implements Runnable{

    FileType fileType;

    String hostname;

    int port;

    @SneakyThrows
    public void run() {
        Socket socket = new Socket(hostname, port);

        BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
        DataOutputStream dos = new DataOutputStream(bos);

        File file = new File(fileType.getPath());
        dos.writeLong(file.length());

        synchronizedFileReading(file, bos);
        bos.close();
        dos.close();
    }

    @SneakyThrows
    private void synchronizedFileReading(File file, BufferedOutputStream bos) {
        switch (fileType){
            case FIRST:
                synchronized (FileType.FIRST){
                    fileReading(file, bos);
                }
                break;
            case SECOND:
                synchronized (FileType.SECOND){
                    fileReading(file, bos);
                }
                break;
            case THIRD:
                synchronized (FileType.THIRD){
                    fileReading(file, bos);
                }
                break;
            default:
                try {
                    throw new FileNotFoundException("Unexpected file");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
        }
    }

    private void fileReading(File file, BufferedOutputStream bos) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fileInputStream);
        int theByte = 0;
        while ((theByte = bis.read()) != -1) bos.write(theByte);
        bis.close();
    }

}
