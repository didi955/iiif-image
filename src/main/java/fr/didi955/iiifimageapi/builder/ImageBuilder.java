package fr.didi955.iiifimageapi.builder;

import fr.didi955.iiifimageapi.factory.QualityFactory;
import fr.didi955.iiifimageapi.factory.RegionFactory;
import fr.didi955.iiifimageapi.factory.RotationFactory;

import java.awt.image.BufferedImage;

public class ImageBuilder {

    private BufferedImage image;

    public ImageBuilder(BufferedImage image) {
        this.image = image;
    }

    public ImageBuilder rotate(String rotation) throws IllegalArgumentException {
        RotationFactory factory = new RotationFactory(this.image);
        this.image = factory.getRotatedImage(rotation);
        return this;
    }

    public ImageBuilder size(String size) throws IllegalArgumentException {
        return this;
    }

    public ImageBuilder region(String region) throws IllegalArgumentException {
        RegionFactory factory = new RegionFactory(this.image);
        this.image = factory.getRegionImage(region);
        return this;
    }

    public ImageBuilder quality(String quality) {
        QualityFactory factory = new QualityFactory(this.image);
        this.image = factory.getQualityImage(quality);
        return this;
    }

    public BufferedImage build() {
        return this.image;
    }


}
