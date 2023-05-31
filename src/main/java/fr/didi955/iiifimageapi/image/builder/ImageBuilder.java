package fr.didi955.iiifimageapi.image.builder;

import fr.didi955.iiifimageapi.exception.BadRequestException;
import fr.didi955.iiifimageapi.image.factory.QualityFactory;
import fr.didi955.iiifimageapi.image.factory.RegionFactory;
import fr.didi955.iiifimageapi.image.factory.RotationFactory;
import fr.didi955.iiifimageapi.image.factory.SizeFactory;

import java.awt.image.BufferedImage;

public class ImageBuilder {

    private BufferedImage image;

    public ImageBuilder(BufferedImage image) {
        this.image = image;
    }

    public ImageBuilder rotate(String rotation) throws BadRequestException {
        RotationFactory factory = new RotationFactory(this.image);
        this.image = factory.getRotatedImage(rotation);
        return this;
    }

    public ImageBuilder size(String size) throws BadRequestException {
        SizeFactory factory = new SizeFactory(this.image);
        this.image = factory.getSizedImage(size);
        return this;
    }

    public ImageBuilder region(String region) throws BadRequestException {
        RegionFactory factory = new RegionFactory(this.image);
        this.image = factory.getRegionImage(region);
        return this;
    }

    public ImageBuilder quality(String quality) throws BadRequestException {
        QualityFactory factory = new QualityFactory(this.image);
        this.image = factory.getQualityImage(quality);
        return this;
    }

    public BufferedImage build() {
        return this.image;
    }


}
