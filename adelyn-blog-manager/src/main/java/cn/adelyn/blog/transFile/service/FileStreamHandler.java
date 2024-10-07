package cn.adelyn.blog.transFile.service;

import java.io.*;

public class FileStreamHandler {

    public static void uploadFile(InputStream inputStream, String destinationPath) throws IOException {
        try (OutputStream outputStream = new FileOutputStream(destinationPath)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer))!= -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
    }

    public static void downloadFile(String destinationPath, OutputStream outputStream) throws IOException {
        try (InputStream inputStream = new FileInputStream(destinationPath)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer))!= -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
    }
}
