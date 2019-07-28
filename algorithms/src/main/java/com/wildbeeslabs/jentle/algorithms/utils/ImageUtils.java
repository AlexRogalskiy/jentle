package com.wildbeeslabs.jentle.algorithms.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

@UtilityClass
public class ImageUtils {

    public static BufferedImage loadImage(final String path) {
        BufferedImage bI = null;
        try {
            bI = ImageIO.read(ImageUtils.class.getResource(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bI;
    }

    @Getter
    @RequiredArgsConstructor
    public static class SpriteSheet {

        private final BufferedImage sheet;

        public BufferedImage crop(int x, int y, int width, int height) {
            return sheet.getSubimage(x, y, width, height);
        }
    }
}
