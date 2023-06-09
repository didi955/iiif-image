package fr.didi955.iiifimage.image.builder;

import fr.didi955.iiifimage.exception.BadRequestException;
import fr.didi955.iiifimage.image.factory.QualityFactory;
import fr.didi955.iiifimage.image.factory.RegionFactory;
import fr.didi955.iiifimage.image.factory.RotationFactory;
import fr.didi955.iiifimage.image.factory.SizeFactory;
import fr.didi955.iiifimage.image.utils.ImageUtil;

import java.awt.image.BufferedImage;

public class ImageBuilder {

    private BufferedImage image;

    public ImageBuilder(BufferedImage image) {
        this.image = ImageUtil.normalize(image);
    }

    /*
    * @param region
     */
    public ImageBuilder region(String region) throws BadRequestException {
        RegionFactory factory = new RegionFactory(this.image);
        this.image = factory.getRegionImage(region);
        return this;
    }

    /*
    * @param size
     */
    public ImageBuilder size(String size) throws Exception {
        SizeFactory factory = new SizeFactory(this.image);
        this.image = factory.getSizedImage(size);
        return this;
    }

    /*
    * @param rotation
     */
    public ImageBuilder rotate(String rotation) throws BadRequestException {
        RotationFactory factory = new RotationFactory(this.image);
        this.image = factory.getRotatedImage(rotation);
        return this;
    }

    /*
    * @param quality
     */
    public void quality(String quality) throws BadRequestException {
        QualityFactory factory = new QualityFactory(this.image);
        this.image = factory.getQualityImage(quality);
    }

    /*
    * Get the built image
    * @return BufferedImage
     */
    public BufferedImage build() {
        return this.image;
    }

}
