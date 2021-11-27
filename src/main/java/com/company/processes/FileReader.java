package com.company.processes;

import com.company.jsonWork.FileType;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.Synchronized;
import lombok.Value;

import java.io.*;
import java.net.Socket;

/** Process 2 daemon */
@Value
public class FileReader implements Runnable{

    FileType fileType;

    @SneakyThrows
    public void run() {
        @Cleanup Socket socket = new Socket(fileType.getHostname(), fileType.getPort());

        @Cleanup BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
        File file = new File(fileType.getPath());

        synchronizedFileReading(file, bufferedOutputStream);
    }

    @SneakyThrows
    @Synchronized("fileType")
    private void synchronizedFileReading(File file, BufferedOutputStream bos) {
        fileReading(file, bos);
    }

    private void fileReading(File file, BufferedOutputStream bufferedOutputStream) throws IOException {
        @Cleanup FileInputStream fileInputStream = new FileInputStream(file);
        @Cleanup BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
        bufferedOutputStream.write(bufferedInputStream.readAllBytes());
    }

}
