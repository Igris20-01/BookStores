package uz.booker.bookstore.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ImageUtil {

    public static byte[] resizeImage(byte[] imageBytes, int targetWidth, int targetHeight) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
        BufferedImage inputImage = ImageIO.read(bis);

        double inputWidth = inputImage.getWidth();
        double inputHeight = inputImage.getHeight();
        double aspectRatio = inputWidth / inputHeight;

        int newWidth = targetWidth;
        int newHeight = (int) (newWidth / aspectRatio);

        if (newHeight > targetHeight) {
            newHeight = targetHeight;
            newWidth = (int) (newHeight * aspectRatio);
        }

        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(inputImage, 0, 0, newWidth, newHeight, null);
        g.dispose();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, "jpg", bos);

        return bos.toByteArray();
    }

}
