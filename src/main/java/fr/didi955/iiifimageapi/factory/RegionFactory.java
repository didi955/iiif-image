package fr.didi955.iiifimageapi.factory;

import java.awt.image.BufferedImage;

public class RegionFactory
{

    private final BufferedImage image;

    public RegionFactory(BufferedImage image)
    {
        this.image = image;
    }

    public BufferedImage getRegionImage(String region) throws IllegalArgumentException {

        if(region.equals("full")) {
            return this.image;
        }
        else if(isRegionValid(region)) {
            String[] regionSplit = region.split(",");
            int x = Integer.parseInt(regionSplit[0]);
            int y = Integer.parseInt(regionSplit[1]);
            int width = Integer.parseInt(regionSplit[2]);
            int height = Integer.parseInt(regionSplit[3]);

            return this.image.getSubimage(x, y, width, height);
        }
        else {
            throw new IllegalArgumentException("Region format is not valid");
        }
    }

    private boolean isRegionValid(String region) {
        return region.matches("^[0-9]+(\\.[0-9]+)?,[0-9]+(\\.[0-9]+)?,[0-9]+(\\.[0-9]+)?,[0-9]+(\\.[0-9]+)?$") || region.equals("full");
    }
}
