package fr.didi955.iiifimage.image.utils;

import fr.didi955.iiifimage.exception.BadRequestException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.PixelDensity;
import org.apache.commons.imaging.common.XmpImagingParameters;
import org.apache.commons.imaging.formats.gif.GifImageParser;
import org.apache.commons.imaging.formats.gif.GifImagingParameters;
import org.apache.commons.imaging.formats.jpeg.JpegImageParser;
import org.apache.commons.imaging.formats.jpeg.JpegImagingParameters;
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


    public static byte[] imageToByteArray(BufferedImage image, String format) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, format, os);
        byte[] bytes = os.toByteArray();
        os.close();
        return bytes;
    }

    public static InputStreamResource imageToInputStreamResource(BufferedImage image, String format) throws ImageWriteException, IOException {
        XmpImagingParameters params;
        InputStreamResource resource = null;

        switch (format) {
            case "tif" -> {
                params = new TiffImagingParameters();
                ((TiffImagingParameters) params).setCompression(TiffConstants.TIFF_COMPRESSION_UNCOMPRESSED);
                params.setPixelDensity(PixelDensity.createUnitless(300, 300)); // TODO: Make this generic
                try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
                    new TiffImageParser().writeImage(image, os, (TiffImagingParameters) params);
                    resource = createInputStreamResource(os.toByteArray());
                }
            }
            case "jpg" -> {
                params = new JpegImagingParameters();
                try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
                    new JpegImageParser().writeImage(image, os, (JpegImagingParameters) params);
                    resource = createInputStreamResource(os.toByteArray());
                }
            }
            case "png" -> {
                params = new PngImagingParameters();
                try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
                    new PngImageParser().writeImage(image, os, (PngImagingParameters) params);
                    resource = createInputStreamResource(os.toByteArray());
                }
            }
            case "gif" -> {
                try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
                    new GifImageParser().writeImage(image, os, new GifImagingParameters());
                    resource = createInputStreamResource(os.toByteArray());
                }
            }
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
            case "jpg" -> MediaType.parseMediaType("image/jpeg");
            case "tif" -> MediaType.parseMediaType("image/tiff");
            case "png" -> MediaType.parseMediaType("image/png");
            case "gif" -> MediaType.parseMediaType("image/gif");
            default -> throw new BadRequestException("Format " + format + " not supported");
        };
    }
}
