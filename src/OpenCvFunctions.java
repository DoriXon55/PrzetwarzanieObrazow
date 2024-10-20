import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

public class OpenCvFunctions {

    public static double userInput;
    ImageDownloader imageDownloader = new ImageDownloader();

    public static void displayOpenImage(String filePath) {
        ImageIcon openImage = new ImageIcon(filePath);
        JLabel openLabel = new JLabel(openImage);
        JFrame frame = new JFrame();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(openLabel);
        frame.pack();
        frame.setVisible(true);
    }

    public void components(Mat image) {

        List<Mat> channelsBGR = new ArrayList<Mat>();
        Core.split(image, channelsBGR);
        saveImage(channelsBGR.get(0), "C:\\Users\\doria\\IdeaProjects\\PrzetwarzanieObrazow_3\\src\\blueImage.jpg");
        saveImage(channelsBGR.get(1), "C:\\Users\\doria\\IdeaProjects\\PrzetwarzanieObrazow_3\\src\\greenImage.jpg");
        saveImage(channelsBGR.get(2), "C:\\Users\\doria\\IdeaProjects\\PrzetwarzanieObrazow_3\\src\\redImage.jpg");
    }


    //sprawdzic czy zadziała.
    public Mat imposition(Mat image) {
        Mat result = new Mat();
        try {
            BufferedImage bufferedImage2 = imageDownloader.downloadImage();
            Mat image2 = imageDownloader.bufferedImageToMat(bufferedImage2);
            Core.addWeighted(image, 0.5, image2, 0.5, 0.0, result);
        } catch (IOException e) {
            System.out.println("Error: Problem w nakladaniu zdjec" + e.getMessage());
        }
        return result;
    }

    public void hsv(Mat image) {
        Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2HSV);
    }

    public void actoins(Mat image) {
        try {
            Mat addedImage = new Mat();
            Mat subtractedImage = new Mat();
            Mat multiplyImage = new Mat();
            Mat divideImage = new Mat();


            BufferedImage bufferedImage2 = imageDownloader.downloadImage();
            Mat image2 = imageDownloader.bufferedImageToMat(bufferedImage2);

            Core.add(image, image2, addedImage);
            Core.subtract(image, image2, subtractedImage);
            Core.multiply(image, image2, multiplyImage);
            Core.divide(image, image2, divideImage);

            Mat imageMinusImage2 = new Mat(); // A\B
            Mat notImage2 = new Mat();
            Core.bitwise_not(image2, notImage2);
            Core.bitwise_and(image, notImage2, imageMinusImage2);

            Mat image2Minusimage = new Mat(); // B\A
            Mat notImage = new Mat();
            Core.bitwise_not(image, notImage);
            Core.bitwise_and(image2, notImage, image2Minusimage);

            saveImage(addedImage, "C:\\Users\\doria\\IdeaProjects\\PrzetwarzanieObrazow_3\\src\\addedImage.jpg");
            saveImage(subtractedImage, "C:\\Users\\doria\\IdeaProjects\\PrzetwarzanieObrazow_3\\src\\subtractedImage.jpg");
            saveImage(multiplyImage, "C:\\Users\\doria\\IdeaProjects\\PrzetwarzanieObrazow_3\\src\\multiplyImage.jpg");
            saveImage(divideImage, "C:\\Users\\doria\\IdeaProjects\\PrzetwarzanieObrazow_3\\src\\divideImage.jpg");
            saveImage(imageMinusImage2, "C:\\Users\\doria\\IdeaProjects\\PrzetwarzanieObrazow_3\\src\\AB.jpg");
            saveImage(image2Minusimage, "C:\\Users\\doria\\IdeaProjects\\PrzetwarzanieObrazow_3\\src\\BA.jpg");

        } catch (IOException e) {
            System.out.println("Error: Problem w nakladaniu zdjec" + e.getMessage());
        }
    }

    public void binarization(Mat image) {
        Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2GRAY);
        Mat binary = new Mat();
        Imgproc.threshold(image, binary, 128, 255, Imgproc.THRESH_BINARY);
        saveImage(binary, "C:\\Users\\doria\\IdeaProjects\\PrzetwarzanieObrazow_3\\src\\'binary.jpg");
    }

    public void normalization(Mat image) {
        Core.MinMaxLocResult imageMinMaxLocResult = Core.minMaxLoc(image);
        Core.normalize(image, image, 0, 255, Core.NORM_MINMAX);
    }

    public void contrast(Mat image, Scanner scanner) {
        System.out.println("O ile chcesz zmienic kontrast?");
        userInput = scanner.nextDouble();
        Core.multiply(image, new Scalar(userInput), image);
    }

    public void brightness(Mat image, Scanner scanner) {
        System.out.println("O ile chcesz rozjasnic zdjecie?");
        userInput = scanner.nextDouble();
        Core.add(image, new Scalar(userInput), image);
    }

    public void histogram(Mat image) {

        List<Mat> images = Collections.singletonList(image);
        Mat histogram = new Mat();
        Imgproc.calcHist(images, new MatOfInt(0), new Mat(), histogram, new MatOfInt(256), new MatOfFloat(0, 256));
    }

    public void saveImage(Mat image, String outputFilePath) {
        boolean result = Imgcodecs.imwrite(outputFilePath, image);
        if (result) {
            System.out.println("Poprawnie zapisano");
            displayOpenImage(outputFilePath);
        } else {
            System.out.println("Nie zapisano pliku");
        }
    }

}
