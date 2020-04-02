package vpntool.gui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class tFile {


    public static void main(String[] args) {
        byte b[] = "abcd\n1234".getBytes();
        try {
            FileOutputStream fos = new FileOutputStream(new File("d:", "1.smv"));
            fos.write(b);
        } catch (IOException e) {
            System.out.println("fail to write the file");
        }

    }
}
