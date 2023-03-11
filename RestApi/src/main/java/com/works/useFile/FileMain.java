package com.works.useFile;

import java.io.File; // Import the File class
import java.io.FileInputStream;
import java.io.FileNotFoundException; // Import this class to handle errors
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner; // Import the Scanner class to read text files


public class FileMain {

    public static void main(String[] args) throws Exception {


        String path = "src/main/resources/resim.jpg";
        FileInputStream fileInputStream = new FileInputStream(path);

        FileChannel fileChannel = fileInputStream.getChannel();
        long size = fileChannel.size();
        ByteBuffer byteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, size);
        CharBuffer charBuffer = StandardCharsets.ISO_8859_1.decode(byteBuffer);

        Scanner scanner = new Scanner(charBuffer.toString());
        boolean status = true;
        while(scanner.hasNext()) {
            String line = scanner.nextLine();
            if ( line.contains("<php") ) {
                status = false;
                break;
            }
            System.out.println(line);
        }

        fileChannel.close();
        fileInputStream.close();

        if ( status == false ) {
            System.err.println("Content File Fail");
        }


    }

}
