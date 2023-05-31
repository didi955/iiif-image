package fr.didi955.iiifimage.image.factory;

import fr.didi955.iiifimage.exception.BadRequestException;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class RotationFactory {

    private final BufferedImage image;

    public RotationFactory(BufferedImage image)
    {
        this.image = image;
    }

    public BufferedImage getRotatedImage(String rotation) throws BadRequestException {

        if(rotation.equals("0")) {
            return this.image;
        }
        else if(isRotationValid(rotation)) {
            double degrees;
            boolean mirror = false;
            if(rotation.startsWith("!")) {
                degrees = Double.parseDouble(rotation.substring(1));
                mirror = true;
            }
            else {
                degrees = Double.parseDouble(rotation);
            }
            return rotateImage(degrees, mirror);
        }
        else {
            throw new BadRequestException("Rotation format is not valid");
        }
    }

    private BufferedImage rotateImage(double degrees, boolean mirror) {
        double radians = Math.toRadians(degrees);
        double sin = Math.abs(Math.sin(radians));
        double cos = Math.abs(Math.cos(radians));
        int width = this.image.getWidth();
        int height = this.image.getHeight();
        int newWidth = (int) Math.floor(width * cos + height * sin);
        int newHeight = (int) Math.floor(height * cos + width * sin);

        BufferedImage rotatedImage = new BufferedImage(newWidth, newHeight, this.image.getType());
        Graphics2D g = rotatedImage.createGraphics();
        AffineTransform at = new AffineTransform();
        at.translate((double) (newWidth - width) / 2, (double) (newHeight - height) / 2);

        if(mirror) {
            at.scale(-1, 1);
            at.translate(-width, 0);
        }

        at.rotate(radians, (double) width / 2, (double) height / 2);
        g.setTransform(at);
        g.drawImage(this.image, 0, 0, null);
        g.dispose();

        return rotatedImage;
    }


    /**
     * Check if rotation parameter is valid
     * @param rotation String : rotation parameter of IIIF request (ex: 0) or (90) or (270) or (!90) or (!180) see <a href="https://iiif.io/api/image/3.0/#43-rotation">IIIF 3.0 specifications</a>
     * @return boolean true if rotation is valid, false otherwise
     */
    private boolean isRotationValid(String rotation) {
        return rotation.matches("^!?[0-9]+(\\.[0-9]+)?$");
    }

}
