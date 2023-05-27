package fr.didi955.iiifimageapi.utils;

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

    public static InputStreamResource imageToInputStreamRessource(BufferedImage image, String format) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, format, os);
        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(os.toByteArray()));
        os.close();
        return resource;
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
            case "jp2" -> MediaType.parseMediaType("image/jp2");
            default -> MediaType.parseMediaType("image/" + format);
        };
    }
}
