package fr.didi955.iiifimage.image.factory;

import fr.didi955.iiifimage.exception.BadRequestException;

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
            try {
                return image.getSubimage(x, y, width, height);
            } catch (Exception e) {
                throw new BadRequestException("The region is outside the raster, so the format is not valid");
            }
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

    public boolean isRegionValid(String region) {
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
            if(regionValues.length == 4){
                double xPercentage = (double) regionValues[0] / 100;
                double yPercentage = (double) regionValues[1] / 100;
                double widthPercentage = (double) regionValues[2] / 100;
                double heightPercentage = (double) regionValues[3] / 100;

                regionValues[0] = (int) (xPercentage * image.getWidth());
                regionValues[1] = (int) (yPercentage * image.getHeight());
                regionValues[2] = (int) (widthPercentage * image.getWidth());
                regionValues[3] = (int) (heightPercentage * image.getHeight());
            }
            else {
                throw new BadRequestException("Region format is not valid");
            }
        }
    }

    private int[] parseRegionValues(String region) {
        String str = region.replace("pct:", "");
        String[] regionSplit = str.split(",");
        int[] regionValues = new int[4];
        for (int i = 0; i < regionValues.length; i++) {
            regionValues[i] = Integer.parseInt(regionSplit[i]);
        }
        return regionValues;
    }
}
