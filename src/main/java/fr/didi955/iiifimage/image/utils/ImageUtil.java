package fr.didi955.iiifimage.image.utils;

import fr.didi955.iiifimage.exception.BadRequestException;
import fr.didi955.iiifimage.exception.OperationNotSupported;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.PixelDensity;
import org.apache.commons.imaging.common.XmpImagingParameters;
import org.apache.commons.imaging.formats.gif.GifImageParser;
import org.apache.commons.imaging.formats.gif.GifImagingParameters;
import org.apache.commons.imaging.formats.png.PngImageParser;
import org.apache.commons.imaging.formats.png.PngImagingParameters;
import org.apache.commons.imaging.formats.tiff.TiffImageParser;
import org.apache.commons.imaging.formats.tiff.TiffImagingParameters;
import org.apache.commons.imaging.formats.tiff.constants.TiffConstants;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageUtil {

    public static BufferedImage normalize(BufferedImage image){
        // Convert all negative image bytes to positive
        int width = image.getWidth();
        int height = image.getHeight();

        int[] signedPixels = image.getRGB(0, 0, width, height, null, 0, width);

        // signed bytes to unsigned bytes
        int[] unsignedPixels = new int[signedPixels.length];
        for (int i = 0; i < signedPixels.length; i++) {
            int alpha = (signedPixels[i] >> 24) & 0xFF;
            int red = (signedPixels[i] >> 16) & 0xFF;
            int green = (signedPixels[i] >> 8) & 0xFF;
            int blue = signedPixels[i] & 0xFF;

            unsignedPixels[i] = (alpha << 24) | (red << 16) | (green << 8) | blue;
        }

        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        image.setRGB(0, 0, width, height, unsignedPixels, 0, width);

        return image;

    }

    public static byte[] imageToByteArray(BufferedImage image, String format) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, format, os);
        byte[] bytes = os.toByteArray();
        os.close();
        return bytes;
    }

    public static InputStreamResource imageToInputStreamResource(BufferedImage image, String format, int densityX, int densityY) throws IOException, ImageWriteException {
        XmpImagingParameters params;
        InputStreamResource resource;

        switch (format) {
            case "tif" -> {
                params = new TiffImagingParameters();
                ((TiffImagingParameters) params).setCompression(TiffConstants.TIFF_COMPRESSION_UNCOMPRESSED);
                params.setPixelDensity(PixelDensity.createUnitless(densityX, densityY));
                try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
                    new TiffImageParser().writeImage(image, os, (TiffImagingParameters) params);
                    resource = createInputStreamResource(os.toByteArray());
                }
            }
            case "jpg" -> {
                try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
                    ImageIO.setUseCache(false);
                    ImageIO.write(image, "jpg", os);
                    resource = createInputStreamResource(os.toByteArray());

                }
            }
            case "png" -> {
                params = new PngImagingParameters();
                params.setPixelDensity(PixelDensity.createUnitless(densityX, densityY));
                try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
                    new PngImageParser().writeImage(image, os, (PngImagingParameters) params);
                    resource = createInputStreamResource(os.toByteArray());
                }
            }
            case "gif" -> {
                params = new GifImagingParameters();
                params.setPixelDensity(PixelDensity.createUnitless(72, 72));
                try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
                    new GifImageParser().writeImage(image, os, (GifImagingParameters) params);
                    resource = createInputStreamResource(os.toByteArray());
                }
            }
            case "jp2" -> throw new OperationNotSupported("Format " + format + " will be soon supported");
            default -> throw new OperationNotSupported("Format " + format + " not supported");
        }
        return resource;
    }

    private static InputStreamResource createInputStreamResource(byte[] byteArray) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        return new InputStreamResource(inputStream);
    }

    /**
     * Parse format to MediaType
     * @param format String : jpg, tif, png, gif according to <a href="https://iiif.io/api/image/3.0/#51-image-information-request">IIIF 3.0 specifications</a>
     * @return MediaType
     */
    public static MediaType parseMediaType(String format) {
        return switch (format) {
            case "jpg" -> MediaType.IMAGE_JPEG;
            case "tif" -> MediaType.parseMediaType("image/tiff");
            case "png" -> MediaType.IMAGE_PNG;
            case "gif" -> MediaType.IMAGE_GIF;
            default -> throw new BadRequestException("Format " + format + " not supported");
        };
    }
}
