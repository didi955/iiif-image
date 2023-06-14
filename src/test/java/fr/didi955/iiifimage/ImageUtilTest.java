package fr.didi955.iiifimage;

import fr.didi955.iiifimage.exception.BadRequestException;
import fr.didi955.iiifimage.image.utils.ImageUtil;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

public class ImageUtilTest {

    @Test
    public void testParseMediaType() {
        assertEquals(MediaType.IMAGE_JPEG, ImageUtil.parseMediaType("jpg"));
        assertEquals(MediaType.IMAGE_PNG, ImageUtil.parseMediaType("png"));
        assertEquals(MediaType.IMAGE_GIF, ImageUtil.parseMediaType("gif"));
        assertEquals(MediaType.parseMediaType("image/tiff"), ImageUtil.parseMediaType("tif"));
    }

    @Test
    public void testParseMediaTypeWithUnsupportedFormat() {
        assertThrows(BadRequestException.class, () -> ImageUtil.parseMediaType("bmp"));
    }

    @Test
    public void testImageToByteArray() throws IOException {
        BufferedImage image = new BufferedImage(100, 124, BufferedImage.TYPE_INT_RGB);
        byte[] bytes = ImageUtil.imageToByteArray(image, "jpg");

        InputStream inputStream = new ByteArrayInputStream(bytes);
        BufferedImage bufferedImage = ImageIO.read(inputStream);

        assertEquals(100, bufferedImage.getWidth());
        assertEquals(124, bufferedImage.getHeight());
    }


}
