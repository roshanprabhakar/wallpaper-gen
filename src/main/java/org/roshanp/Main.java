package org.roshanp;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Objects;

public class Main {

    public static void main(String[] args) {

        File res = new File("src/main/resources");
        Gen gen = new Gen(new File("src/main/resources/gradient2.jpg"), res.listFiles(), 4, 2, 0.35, 0.35, 100, 100);
//
//        try {
//            BufferedImage i = ImageIO.read(new File("src/main/resources/SavageMode.jpg"));
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
