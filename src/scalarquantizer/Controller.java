
package scalarquantizer;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;
import javax.imageio.ImageIO;

public class Controller {

 
    public static void main(String[] args) {
        vector_Quantizer c =new vector_Quantizer();

        c.compress(4,2,2);
        Node n = new Node();
        
        LinkedList<int[][]> dec = new LinkedList<>();
        for (int i = 0; i < n.Decompress().size(); i++) {
            int x[][] = new int[2][2];
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    x[j][k] = n.Decompress().get(i)[j][k].intValue();
                }
            }
            dec.add(x);
            
        }
        
      int image[][]= c.Transform(dec,c.rowsizeofpaded , c.colsizeofpaded);
       
      System.out.println("==========bassam=========");
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                System.out.print(image[i][j]+"    ");
            }
            System.out.println();
        }
        writeImage(image, "D:\\gggggggg.jpg");
        
        
    }
    
    
    
    
   

    public static int[][] readImage(String path) {

        BufferedImage img;
        try {
            img = ImageIO.read(new File(path));

            int hieght = img.getHeight();
            int width = img.getWidth();

            int[][] imagePixels = new int[hieght][width];
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < hieght; y++) {

                    int pixel = img.getRGB(x, y);

                    int red = (pixel & 0x00ff0000) >> 16;
                    int grean = (pixel & 0x0000ff00) >> 8;
                    int blue = pixel & 0x000000ff;
                    int alpha = (pixel & 0xff000000) >> 24;
                    imagePixels[y][x] = red;
                }
            }

            return imagePixels;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            return null;
        }

    }

    public static void writeImage(int[][] imagePixels, String outPath) {

        BufferedImage image = new BufferedImage(imagePixels[0].length, imagePixels.length, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < imagePixels.length; y++) {
            for (int x = 0; x < imagePixels[y].length; x++) {
                int value = -1 << 24;
                value = 0xff000000 | (imagePixels[y][x] << 16) | (imagePixels[y][x] << 8) | (imagePixels[y][x]);
                image.setRGB(x, y, value);

            }
        }

        File ImageFile = new File(outPath);
        try {
            ImageIO.write(image, "jpg", ImageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int log(int x, int base) {
        return (int) (Math.log(x) / Math.log(base));
    }

}
