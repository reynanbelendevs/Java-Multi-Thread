import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static final String SourceFile = "./resources/sample.jpg";
    public static final String Destination_File  = "./out/sample.jpg";
    public static void main(String[] args) throws IOException, InterruptedException {
        BufferedImage originalImage;
        originalImage = ImageIO.read(new File(SourceFile));
        BufferedImage resultImage = new BufferedImage(originalImage.getWidth() , originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        long StartTime = System.currentTimeMillis();
//        recolorSingleThreaded(originalImage,resultImage);
        recolorMultiThreaded(originalImage, resultImage , 4);
        long endTime = System.currentTimeMillis();
        System.out.println("duration" + " "+ (endTime - StartTime));
        File outFile = new File(Destination_File);
        ImageIO.write(resultImage , "jpg" , outFile);
    }
    public  static  void recolorMultiThreaded(BufferedImage originalImage,BufferedImage resultImage, int numberofThreads ) throws InterruptedException {
        List<Thread> threads = new ArrayList<>();
        int width = originalImage.getWidth();
        int height = originalImage.getHeight() / numberofThreads;

        for(int i = 0  ; i < numberofThreads; i++){
            final int threadMultiplier = i;
            Thread thread = new Thread(()-> {
                int lefCorner = 0;
                int topCorner = height * threadMultiplier;
                recolorImage(originalImage, resultImage , lefCorner , topCorner , width , height);
            });
             threads.add(thread);
        }
        for(Thread thread : threads){
            thread.start();
        }
        for (Thread thread: threads){
            thread.join();
        }
    }

    public static  void recolorSingleThreaded (BufferedImage originalImage, BufferedImage resulImage){
        recolorImage(originalImage , resulImage, 0 ,0, originalImage.getWidth() ,originalImage.getHeight());
    }
    public static void recolorImage(BufferedImage originalImage, BufferedImage resultImage, int leftCorner, int topCorner, int width , int height){
    for(int x = leftCorner; x < leftCorner + width && x < originalImage.getWidth() ; x++ ){
        for(int y = topCorner; y < topCorner + height && y < originalImage.getHeight(); y++){
            recolorPixel(originalImage, resultImage, x , y );
            }
        }
    }
    public static void recolorPixel(BufferedImage originalImage , BufferedImage resultImage, int x , int y){
        int rgb = originalImage.getRGB(x , y);
        System.out.println("RGB COLOR " + rgb);
        int red = getRed(rgb);
        int green = getGreen(rgb);
          int blue = getBlue(rgb);
        int newRed;
        int newGreen;
        int newBlue;
        if(isShadeOfGray(red , green , blue)){
            newRed = Math.min(255, red + 10);
            newGreen = Math.max(0, green - 80);
            newBlue = Math.max(0, blue - 20);
        }else {
            newRed = red;
            newGreen= green;
            newBlue = blue;
        }
        int newRGB = createRGBFromColors(newRed , newGreen , newBlue);
        setRGB(resultImage , x , y, newRGB);
    }
    public  static boolean isShadeOfGray(int red, int green, int blue){
        return Math.abs(red - green ) < 30 && Math.abs(red - blue) < 30 && Math.abs(green - blue ) < 30;
    }
    public  static  void setRGB(BufferedImage originalImage , int x, int y, int rgb){
        originalImage.getRaster().setDataElements(x , y , originalImage.getColorModel().getDataElements(rgb , null));

    }
    public  static int createRGBFromColors(int red, int green, int blue){
        int rgb = 0;
        rgb |= blue;
        rgb |= green << 8;
        rgb |= red << 16;
        rgb |= 0xFF000000;
        System.out.println("Create From Colors: " + rgb);
        return  rgb;
    }
    public static int getRed(int rgb){
        System.out.println("From red:" + rgb);
        System.out.println(rgb & 0x00FF0000 >> 16);
        return  rgb & 0x00FF0000 >> 16;
    }
    public static int getGreen(int rgb){
        System.out.println("From Green: " + rgb);
        System.out.println(rgb & 0x0000FF00 >> 8);
        return  rgb & 0x0000FF00 >> 8;
    }
    public static int getBlue(int rgb){
        System.out.println("From Blue" + rgb);
        System.out.println(rgb & 0x000000FF);
        return  rgb & 0x000000FF;
    }
}