package fr.didi955.iiifimage.image.factory;

import fr.didi955.iiifimage.exception.BadRequestException;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SizeFactory {

    private final BufferedImage image;

    public SizeFactory(BufferedImage image) {
        this.image = image;
    }

    public BufferedImage getSizedImage(String size) {

        if(!isValidSize(size)){
            throw new BadRequestException("Invalid size: " + size);
        }
        if(size.equalsIgnoreCase("max")){
            return this.image;
        }
        /*
        else if(size.equalsIgnoreCase("^max")){
            ImageInfo imageInfo = new ImageInfo(this.image);
            int maxWitdh = imageInfo.getMaxWidth();
            int maxHeight = imageInfo.getMaxHeight();
            return scale(maxWitdh, maxHeight);
        }
        else if(isScaledWidth(size)){
            int width = extractWidth(size);
            if(width > this.image.getWidth()){
                throw new BadRequestException("Size greater than the region image size, use upscaling: " + size);
            }
            return scale(width, null, true);
        }
        else if(isScaledWidthUpscale(size)){
            int width = extractWidth(size);
            return scale(width, null, true);
        }
        else if(isScaledHeight(size)){
            int height = extractHeight(size);
            if(height > this.image.getHeight()){
                throw new BadRequestException("Size greater than the region image size, use upscaling: " + size);
            }
            return scale(null, height, true);
        }
        else if(isScaledHeightUpscale(size)){
            int height = extractHeight(size);
            return scale(null, height, true);
        }
        else if(isScaledPercentage(size)){
            int percentage = extractPercentage(size);
            int width = (int) (this.image.getWidth() * (percentage / 100.0));
            int height = (int) (this.image.getHeight() * (percentage / 100.0));
            return scale(width, height);
        }
        else if(isScaledPercentageUpscale(size)){
            int percentage = extractPercentage(size);
            int width = (int) (this.image.getWidth() * (percentage / 100.0));
            int height = (int) (this.image.getHeight() * (percentage / 100.0));
            return scale(width, height);
        }
        else if(isExactWidthHeight(size)){
            int width = extractWidth(size);
            int height = extractHeight(size);
            if(width > this.image.getWidth() || height > this.image.getHeight()){
                throw new BadRequestException("Size greater than the region image size, use upscaling: " + size);
            }
            return scale(width, height);
        }
        else if(isExactWidthHeightUpscale(size)){
            int width = extractWidth(size);
            int height = extractHeight(size);
        }
        else if(isScaledWidthHeight(size)){

        }
        else if(isScaledWidthHeightUpscale(size)){

        }

         */

        return null;
    }

    private BufferedImage scale(int width, int height){
        BufferedImage scaledImage = new BufferedImage(width, height, this.image.getType());
        Graphics2D graphics2D = scaledImage.createGraphics();
        graphics2D.drawImage(this.image, 0, 0, width, height, null);
        graphics2D.dispose();
        return scaledImage;
    }

    private BufferedImage scale(Integer width, Integer height, boolean conserveRatioLarger) throws Exception {
        if (width == null && height == null) {
            throw new Exception("One of width or height must be not null");
        }
        if(conserveRatioLarger) {
            // IF with is not null, scale the image to that width and conserve the ratio
            if (width != null && height == null) {
                double ratio = (double) this.image.getWidth() / (double) this.image.getHeight();
                height = (int) (width / ratio);
            }
            // IF height is not null, scale the image to that height and conserve the ratio
            else if (height != null && width == null) {
                double ratio = (double) this.image.getHeight() / (double) this.image.getWidth();
                width = (int) (height / ratio);
            }
        }
        else if(width == null || height == null){
            throw new Exception("Width and height cannot be null");
        }
        return scale(width, height);
    }

    public static boolean isScaledWidth(String size){
        return size.matches("^[0-9]+,$");
    }

    public static boolean isScaledWidthUpscale(String size){
        return size.matches("^\\^[0-9]+,$");
    }

    public static boolean isScaledHeight(String size){
        return size.matches("^,[0-9]+$");
    }

    public static boolean isScaledHeightUpscale(String size){
        return size.matches("^\\^,[0-9]+$");
    }

    public static boolean isScaledPercentage(String size){
        return size.matches("^pct:([1-9][0-9]?|100)$");
    }

    public static boolean isScaledPercentageUpscale(String size){
        return size.matches("^\\^pct:([1-9]|[1-9][0-9]|[1-9][0-9][0-9])$");
    }

    public static boolean isExactWidthHeight(String size){
        return size.matches("^[0-9]+,[0-9]+$");
    }

    public static boolean isExactWidthHeightUpscale(String size){
        return size.matches("^\\^[0-9]+,[0-9]+$");
    }

    public static boolean isScaledWidthHeight(String size){
        return size.matches("^![0-9]+,[0-9]+,?$");
    }

    public static boolean isScaledWidthHeightUpscale(String size){
        return size.matches("^\\^![0-9]+,[0-9]+,?$");
    }

    public static boolean isValidSize(String size){
        return size.equalsIgnoreCase("max") || size.equalsIgnoreCase("^max") || isScaledWidth(size) || isScaledWidthUpscale(size) || isScaledHeight(size) || isScaledHeightUpscale(size) || isScaledPercentage(size) || isScaledPercentageUpscale(size) || isExactWidthHeight(size) || isExactWidthHeightUpscale(size) || isScaledWidthHeight(size) || isScaledWidthHeightUpscale(size);
    }

    private int extractPercentage(String size){
        return Integer.parseInt(size.substring(size.length() - 1));
    }

    private int parseInteger(String s) throws BadRequestException {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Failed to parse integer: " + s);
        }
    }
}
