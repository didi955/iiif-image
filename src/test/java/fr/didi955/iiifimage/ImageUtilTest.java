package fr.didi955.iiifimage;

import fr.didi955.iiifimage.exception.BadRequestException;
import fr.didi955.iiifimage.image.utils.ImageUtil;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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


}
