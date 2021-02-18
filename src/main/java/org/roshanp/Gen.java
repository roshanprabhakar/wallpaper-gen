package org.roshanp;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Gen {

    private BufferedImage gradient;
    private BufferedImage[] covers;

    private int cellWidth;
    private int cellHeight;
    private int cellPaddingSide;
    private int cellPaddingUp;

    private int coverWidth;

    private int rosterWidth;
    private int rosterHeight;

    private ArrayList<Point> pos;

    public Gen(File background, File[] covers, int r, int c, double sp, double up, int rosterMarginWidth, int rosterMarginHeight) {

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

        //define cell information for positions
        cellWidth = (int) ((screen.getWidth() - rosterMarginWidth * 2) / r);
        cellHeight = (int) ((screen.getHeight() - rosterMarginHeight * 2) / c);

        cellPaddingSide = (int) (cellWidth * sp);
        cellPaddingUp = (int) (cellHeight * up);

        if (cellWidth > cellHeight) {
            coverWidth = cellHeight - 2 * cellPaddingUp;
        } else {
            coverWidth = cellWidth - 2 * cellPaddingSide;
        }

        pos = new ArrayList<>(); //holds all cover top left positions
        for (int y = rosterMarginHeight; y <= screen.getHeight() - rosterMarginHeight - cellHeight; y += cellHeight) {
            for (int x = rosterMarginWidth; x <= screen.getWidth() - rosterMarginWidth - cellWidth; x += cellWidth) {
                pos.add(new Point(
                        x + cellWidth / 2 - coverWidth / 2,
                        y + cellHeight / 2 - coverWidth / 2));
            }
        }

        //initialize background
        BufferedImage bg = null;
        try {
            bg = resize(ImageIO.read(background), (int) screen.getWidth(), (int) screen.getHeight());
        } catch (IOException e) {
//            System.out.println("could not read background image");
            e.printStackTrace();
        }
        assert bg != null;

        //read all images
        ArrayList<BufferedImage> cd = new ArrayList<>();
        for (int i = 0; i < covers.length; i++) {
            try {
                if (!covers[i].getAbsolutePath().contains(".DS_Store") && !covers[i].getAbsolutePath().contains(background.getAbsolutePath()) && !covers[i].getAbsolutePath().contains("gradient")) {
                    cd.add(resize(ImageIO.read(covers[i]), coverWidth, coverWidth));
                }
            } catch (IOException e) {
                System.out.println("could not read cover file: " + covers[i].getName());
            }
        }
        Collections.shuffle(cd);

        int i = 0;
        while (i < cd.size() && i < pos.size()) {
            Point loc = pos.get(i);
            BufferedImage cover = cd.get(i);
            for (int y = (int) loc.getY(); y < loc.getY() + coverWidth; y++) {
                for (int x = (int) loc.getX(); x < loc.getX() + coverWidth; x++) {
                    bg.setRGB(x, y, cover.getRGB(x - (int) loc.getX(), y - (int) loc.getY()));
                }
            }
            i++;
        }

//        display(bg);

        try {
            ImageIO.write(bg, "png", new File("wallpaper.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    public static void drawRect(BufferedImage image, int x, int y) {
        for (int r = y - 3; r < y + 3; r++) {
            for (int c = x - 3; c < x + 3; c++) {
                image.setRGB(c, r, Color.WHITE.getRGB());
            }
        }
    }

    public static void display(BufferedImage image) {
        JFrame frame = new JFrame("image");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new JLabel(new ImageIcon(image)));
        frame.setUndecorated(true);
        frame.pack();
        frame.setVisible(true);
    }


}
