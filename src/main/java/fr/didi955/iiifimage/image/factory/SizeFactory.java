package fr.didi955.iiifimage.image.factory;

import fr.didi955.iiifimage.exception.BadRequestException;
import fr.didi955.iiifimage.image.ImageController;
import fr.didi955.iiifimage.image.builder.ImageBuilder;
import fr.didi955.iiifimage.image.entity.ImageInfo;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SizeFactory {

    private final BufferedImage image;
    private final ImageBuilder builder;

    public SizeFactory(ImageBuilder builder, BufferedImage image) {
        this.image = image;
        this.builder = builder;
    }

    public BufferedImage getSizedImage(String size) throws Exception {

        if(!isValidSize(size)){
            throw new BadRequestException("Invalid size: " + size);
        }
        if(size.equalsIgnoreCase("max")){
            return this.image;
        }
        else if(size.equalsIgnoreCase("^max")){
            int maxWidth = this.builder.getOriginalWidth();
            int maxHeight = this.builder.getOriginalHeight();
            return scale(maxWidth, maxHeight);
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
            if(percentage > 100){
                throw new BadRequestException("Size greater than the region image size, use upscaling: " + size);
            }
            int width = (int) (this.image.getWidth() * (percentage / 100.0));
            int height = (int) (this.image.getHeight() * (percentage / 100.0));
            return scale(width, height);
        }
        else if(isScaledPercentageUpscale(size)){
            int percentage = extractPercentage(size);
            int width = this.image.getWidth() * (percentage / 100);
            int height = this.image.getHeight() * (percentage / 100);
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
            int[] sizes = extractWidthHeight(size);
            int width = sizes[0];
            int height = sizes[1];
            return scale(width, height);
        }
        else if(isScaledWidthHeight(size)){
            int width = this.image.getWidth();
            int height = this.image.getHeight();
            return scaleRatio(width, height);
        }
        else if(isScaledWidthHeightUpscale(size)){
            int[] sizes = extractWidthHeight(size);
            int width = sizes[0];
            int height = sizes[1];
            return scaleRatio(width, height);
        }

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

    public BufferedImage scaleRatio(int targetWidth, int targetHeight) {
        int imgWidth = this.image.getWidth();
        int imgHeight = this.image.getHeight();

        if (imgWidth*targetHeight > targetWidth*imgHeight) {
            imgHeight = imgHeight * targetWidth / imgWidth;
            imgWidth = targetWidth;
        } else {
            imgWidth = imgWidth * targetHeight / imgHeight;
            imgHeight = targetHeight;
        }

        Image scaledImage = this.image.getScaledInstance(imgWidth, imgHeight, Image.SCALE_SMOOTH);
        BufferedImage resizedImg = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = resizedImg.createGraphics();
        g2d.drawImage(scaledImage, 0, 0, null);
        g2d.dispose();

        return resizedImg;
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

    /*
    * Extracts the width and height from a string
    *
    * @param size The string to extract the width and height from
    * @return An array containing the width and height
    * @throws BadRequestException If the width and height cannot be extracted
     */
    private int extractWidth(String size) throws BadRequestException {
        String[] split = size.split(",");
        Pattern pattern = Pattern.compile("([0-9]+)");
        Matcher matcher = pattern.matcher(split[0]);
        if(matcher.matches()){
            return parseInteger(matcher.group(1));
        }
        throw new BadRequestException("Failed to extract width from size: " + size);
    }

    /*
    * Extracts the height from a string
    *
    * @param size The string to extract the height from
    * @return The height
    * @throws BadRequestException If the height cannot be extracted
     */
    private int extractHeight(String size) throws BadRequestException {
        String[] split = size.split(",");
        Pattern pattern = Pattern.compile("([0-9]+)");
        Matcher matcher = pattern.matcher(split[1]);
        if(matcher.matches()){
            return parseInteger(matcher.group(1));
        }
        throw new BadRequestException("Failed to extract height from size: " + size);
    }

    /*
    * Extracts the width and height from a string
    *
    * @param size The string to extract the width and height from
    * @return An array containing the width and height
    * @throws BadRequestException If the width and height cannot be extracted
     */
    private int[] extractWidthHeight(String size) throws BadRequestException {
        Pattern pattern = Pattern.compile("([0-9]+),([0-9]+)");
        Matcher matcher = pattern.matcher(size);
        if(matcher.find()){
            ImageController.LOGGER.info("Width: " + matcher.group(1));
            int width = parseInteger(matcher.group(1));
            int height = parseInteger(matcher.group(2));
            return new int[]{width, height};
        }
        ImageController.LOGGER.info("Failed to extract width and height from size: " + size);
        throw new BadRequestException("Failed to extract width from size: " + size);
    }

    /*
    * Extracts the percentage from a string
    */
    private int extractPercentage(String size){
        return Integer.parseInt(size.replace("pct:", "").replace("^", ""));
    }

    /*
    * Parses an integer from a string
    *
    * @param s The string to parse
    * @return The parsed integer
    * @throws BadRequestException If the string could not be parsed
     */
    private int parseInteger(String s) throws BadRequestException {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Failed to parse integer: " + s);
        }
    }
}
