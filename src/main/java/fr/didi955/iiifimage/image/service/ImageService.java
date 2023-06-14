package fr.didi955.iiifimage.image.service;

import fr.didi955.iiifimage.image.ImageController;
import fr.didi955.iiifimage.exception.BadRequestException;
import fr.didi955.iiifimage.exception.OperationNotSupported;
import fr.didi955.iiifimage.image.builder.ImageBuilder;
import fr.didi955.iiifimage.image.entity.Image;
import fr.didi955.iiifimage.image.entity.ImageInfo;
import fr.didi955.iiifimage.image.helpers.ImageHelper;
import fr.didi955.iiifimage.image.utils.ImageUtil;
import org.apache.commons.imaging.Imaging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class ImageService {

    @Value("${image.path}")
    private String imagePath;

    public ResponseEntity<InputStreamResource> getImage(String inventoryNumber, String region, String size, String rotation, String quality, String format) throws ResponseStatusException {
        try {
            BufferedImage image = ImageHelper.fetchImage(inventoryNumber, imagePath);
            ImageBuilder builder = new ImageBuilder(image);
            builder.region(region).size(size).rotate(rotation).size(size).quality(quality);
            BufferedImage result = builder.build();
            result = ImageUtil.normalize(result);
            org.apache.commons.imaging.ImageInfo imageInfo = Imaging.getImageInfo(ImageUtil.imageToByteArray(result, format));
            InputStreamResource inputStream = ImageUtil.imageToInputStreamResource(result, format, imageInfo.getPhysicalWidthDpi(), imageInfo.getPhysicalHeightDpi());

            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(ImageUtil.parseMediaType(format))
                    .body(inputStream);
        }
        catch (BadRequestException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        catch (OperationNotSupported e) {
            throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, e.getMessage());
        } catch (Exception e) {
            ImageController.LOGGER.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to process image");
        }
    }

    public ImageInfo getImageInfo(String inventoryNumber) throws ResponseStatusException {

        try {
            BufferedImage resource = ImageHelper.fetchImage(inventoryNumber, imagePath);
            Image image = new Image(inventoryNumber, resource.getWidth(), resource.getHeight(), "tiff");
            return new ImageInfo(image);
        }
        catch (BadRequestException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
