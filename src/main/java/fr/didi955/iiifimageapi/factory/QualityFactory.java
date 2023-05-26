package fr.didi955.iiifimageapi.factory;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;

public class QualityFactory {

    private final BufferedImage image;

    public QualityFactory(BufferedImage image)
    {
        this.image = image;
    }

    public BufferedImage getQualityImage(String quality) throws IllegalArgumentException {

        if(quality.equals("default")) {
            return this.image;
        }
        else if(isQualityValid(quality)) {
            return processQuality(quality);
        }
        else {
            throw new IllegalArgumentException("Quality format is not valid");
        }
    }

    private boolean isQualityValid(String quality) {
        return quality.equals("color") || quality.equals("gray") || quality.equals("bitonal");
    }

    private BufferedImage processQuality(String quality){
        return switch (quality) {
            case "color" -> this.image;
            case "gray" -> gray();
            case "bitonal" -> bitonal();
            default -> throw new IllegalArgumentException("Quality format is not valid");
        };


    }

     private BufferedImage gray(){
         BufferedImage grayscaleImage = new BufferedImage(this.image.getWidth(), this.image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
         Graphics2D g2d = grayscaleImage.createGraphics();
         g2d.drawImage(this.image, 0, 0, null);
         g2d.dispose();
         return grayscaleImage;
     }

     private BufferedImage bitonal(){
         BufferedImage bitonalImage = new BufferedImage(this.image.getWidth(), this.image.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
         ColorConvertOp colorConvert = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
         colorConvert.filter(this.image, bitonalImage);
         return bitonalImage;
     }


}
