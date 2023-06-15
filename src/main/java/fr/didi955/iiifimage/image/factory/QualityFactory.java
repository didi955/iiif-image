package fr.didi955.iiifimage.image.factory;

import fr.didi955.iiifimage.exception.BadRequestException;

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

    public BufferedImage getQualityImage(String quality) throws BadRequestException {

        if(quality.equalsIgnoreCase("default")) {
            return this.image;
        }
        else if(isQualityValid(quality)) {
            return processQuality(quality);
        }
        return null;
    }

    private boolean isQualityValid(String quality) {
        return quality.equals("color") || quality.equals("gray") || quality.equals("bitonal") || quality.equals("default");
    }

    private BufferedImage processQuality(String quality) throws BadRequestException {
        return switch (quality) {
            case "color" -> color();
            case "gray" -> gray();
            case "bitonal" -> bitonal();
            default -> throw new BadRequestException("Quality format is not valid");
        };
    }

     private BufferedImage gray(){
         BufferedImage grayscaleImage = new BufferedImage(this.image.getWidth(), this.image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
         Graphics2D g2d = grayscaleImage.createGraphics();
         g2d.drawImage(this.image, 0, 0, null);
         g2d.dispose();
         return grayscaleImage;
     }

     private BufferedImage color(){
         BufferedImage colorImage = new BufferedImage(this.image.getWidth(), this.image.getHeight(), BufferedImage.TYPE_INT_RGB);
         Graphics2D g2d = colorImage.createGraphics();
         g2d.drawImage(this.image, 0, 0, null);
         g2d.dispose();
         return colorImage;
     }

     private BufferedImage bitonal(){
         BufferedImage bitonalImage = new BufferedImage(this.image.getWidth(), this.image.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
         ColorConvertOp colorConvert = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
         colorConvert.filter(this.image, bitonalImage);
         return bitonalImage;
     }


}
