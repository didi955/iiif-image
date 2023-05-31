package fr.didi955.iiifimage;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import fr.didi955.iiifimage.exception.BadRequestException;
import fr.didi955.iiifimage.exception.OperationNotSupported;
import fr.didi955.iiifimage.image.utils.ImageUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.awt.image.BufferedImage;

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

    /* 
     
     @Test
    public void testImageToInputStreamResource() throws IOException, ImageWriteException {
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(new byte[1024]));
        InputStreamResource resource = ImageUtil.imageToInputStreamResource(image, "png");
        assertNotNull(resource);
    } 
     
    */

    @Test
    public void testImageToInputStreamResourceWithUnsupportedFormat() {
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        assertThrows(OperationNotSupported.class, () -> ImageUtil.imageToInputStreamResource(image, "bmp"));
    }

    /* 

     @Test
    public void testImageToByteArray() throws IOException {
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(new byte[1024]));
        byte[] bytes = ImageUtil.imageToByteArray(image, "jpg");
        assertArrayEquals(new byte[1024], bytes);
    }

    */


}
