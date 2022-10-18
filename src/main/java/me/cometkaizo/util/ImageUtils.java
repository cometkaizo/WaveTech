package me.cometkaizo.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@SuppressWarnings("unused")
public class ImageUtils {

    /**
     * Credit: Mr. Polywhirl <a href="https://stackoverflow.com/questions/11006394/is-there-a-simple-way-to-compare-bufferedimage-instances">src0</a>
     * Compares two images pixel by pixel.
     *
     * @param imgA the first image.
     * @param imgB the second image.
     * @return whether the images are both the same or not.
     */
    public static boolean compareImages(BufferedImage imgA, BufferedImage imgB) {
        // The images must be the same size.
        if (imgA.getWidth() != imgB.getWidth() || imgA.getHeight() != imgB.getHeight()) {
            return false;
        }

        int width = imgA.getWidth();
        int height = imgA.getHeight();

        // Loop over every pixel.
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Compare the pixels for equality.
                if (imgA.getRGB(x, y) != imgB.getRGB(x, y)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * credit: Klark
     */
    @Contract("null -> null")
    public static @Nullable BufferedImage deepCopyImage(BufferedImage bi) {
        if (bi == null) {
            return null;
        }
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    public static Optional<BufferedImage> readImage(File file) {
        try {
            return Optional.of(ImageIO.read(file));
        } catch (IOException e) {
            LogUtils.error("Could not find file at location '{}'", file.getPath());
            return Optional.empty();
        }
    }

    public static @NotNull BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return resizedImage;
    }

    /**
     * Credit: java2s <a href="http://www.java2s.com/example/java/2d-graphics/bufferedimage-hash.html">src0</a>
     * Returns a non instance dependant hash code for the buffered image.
     * Two buffered images with the same contents will both always output the same integer
     * @param image the image to get the hash for
     * @return a non instance dependant hash code
     */
    public static int contentsHash(BufferedImage image) {
        return Arrays.hashCode(image.getRGB(
                0, 0, image.getWidth(), image.getHeight(),
                null, 0, image.getWidth())
        );
    }
}
