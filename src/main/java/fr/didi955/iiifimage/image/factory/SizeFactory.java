package fr.didi955.iiifimage.image.factory;

import fr.didi955.iiifimage.exception.BadRequestException;

import java.awt.image.BufferedImage;

public class SizeFactory {

    private final BufferedImage image;

    public SizeFactory(BufferedImage image) {
        this.image = image;
    }

    public BufferedImage getSizedImage(String size) throws BadRequestException {

        if(size.equals("max")){
            return this.image;
        }
        else if(size.startsWith("^")){
            throw new BadRequestException("Upscaling is not supported");
        }
        else if(isSizeValid(size)){
            int[] sizeValues = parseSizeValues(size);
            int width = sizeValues[0];
            int height = sizeValues[1];
            return this.image.getSubimage(0, 0, width, height);
        }
        else {
            throw new BadRequestException("Size format is not valid");
        }

    }

    private boolean isSizeValid(String size){
        String regex = "^[0-9]+(\\.[0-9]+)?,[0-9]+(\\.[0-9]+)?$";
        if(size.equals("max")){
            return true;
        }
        else if(size.matches(regex)){
            int[] sizeValues = parseSizeValues(size);
            int width = sizeValues[0];
            int height = sizeValues[1];
            return width <= this.image.getWidth() && height <= this.image.getHeight();
        }
        return false;
    }

    private int[] parseSizeValues(String size){
        String[] sizeSplit = size.split(",");
        int[] sizeValues = new int[2];
        for(int i = 0; i < 2; i++){
            sizeValues[i] = Integer.parseInt(sizeSplit[i]);
        }
        return sizeValues;
    }
}
