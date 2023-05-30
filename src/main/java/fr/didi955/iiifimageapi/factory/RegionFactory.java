package fr.didi955.iiifimageapi.factory;

import fr.didi955.iiifimageapi.exception.BadRequestException;

import java.awt.image.BufferedImage;

public class RegionFactory {

    private final BufferedImage image;

    public RegionFactory(BufferedImage image) {
        this.image = image;
    }

    public BufferedImage getRegionImage(String region) {
        if (region.equals("full")) {
            return image;
        } else if (region.equals("square")) {
            return getSquareRegionImage();
        } else if (isRegionValid(region)) {
            int[] regionValues = parseRegionValues(region);
            adjustRegionValues(region, regionValues);
            int x = regionValues[0];
            int y = regionValues[1];
            int width = regionValues[2];
            int height = regionValues[3];
            return image.getSubimage(x, y, width, height);
        } else {
            throw new BadRequestException("Region format is not valid");
        }
    }

    private BufferedImage getSquareRegionImage() {
        int width = image.getWidth();
        int height = image.getHeight();
        int x = 0;
        int y = 0;
        if (width > height) {
            x = (width - height) / 2;
            width = height;
        } else if (height > width) {
            y = (height - width) / 2;
            height = width;
        }
        return image.getSubimage(x, y, width, height);
    }

    private boolean isRegionValid(String region) {
        String regex = "^(pct:)?[0-9]+(\\.[0-9]+)?,[0-9]+(\\.[0-9]+)?,[0-9]+(\\.[0-9]+)?,[0-9]+(\\.[0-9]+)?$";
        return region.equals("full") || region.equals("square") || (region.matches(regex) && isWithinImageBounds(region));
    }

    private boolean isWithinImageBounds(String region) {
        int[] regionValues = parseRegionValues(region);
        adjustRegionValues(region, regionValues);
        int x = regionValues[0];
        int y = regionValues[1];
        int width = regionValues[2];
        int height = regionValues[3];
        return x <= image.getWidth() && y <= image.getHeight() && width <= image.getWidth() && height <= image.getHeight();
    }

    private void adjustRegionValues(String region, int[] regionValues) {
        if (region.startsWith("pct:")) {
            int imageWidth = image.getWidth();
            for (int i = 0; i < regionValues.length; i++) {
                regionValues[i] = (int) (regionValues[i] * imageWidth / 100.0);
            }
        }
    }

    private int[] parseRegionValues(String region) {
        region = region.replace("pct:", "");
        String[] regionSplit = region.split(",");
        int[] regionValues = new int[4];
        for (int i = 0; i < regionValues.length; i++) {
            regionValues[i] = Integer.parseInt(regionSplit[i]);
        }
        return regionValues;
    }
}
