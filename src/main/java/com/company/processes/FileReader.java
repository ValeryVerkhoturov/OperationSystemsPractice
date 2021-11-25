package com.company.processes;

import com.company.jsonWork.FileType;
import lombok.SneakyThrows;
import lombok.Value;

import java.io.*;
import java.net.Socket;

/** Process 2 daemon */
@Value
public class FileReader implements Runnable{

    FileType fileType;

    @SneakyThrows
    public void run() {
        Socket socket = new Socket(fileType.getHostname(), fileType.getPort());

        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
        DataOutputStream dataOutputStream = new DataOutputStream(bufferedOutputStream);

        File file = new File(fileType.getPath());
        dataOutputStream.writeLong(file.length());


        synchronizedFileReading(file, bufferedOutputStream);
        bufferedOutputStream.close();
        dataOutputStream.close();
    }

    @SneakyThrows
    private void synchronizedFileReading(File file, BufferedOutputStream bos) {
        synchronized (fileType){
            fileReading(file, bos);
        }
    }

    private void fileReading(File file, BufferedOutputStream bufferedOutputStream) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
        int theByte = 0;
        while ((theByte = bufferedInputStream.read()) != -1) bufferedOutputStream.write(theByte);
        bufferedInputStream.close();
    }

}
