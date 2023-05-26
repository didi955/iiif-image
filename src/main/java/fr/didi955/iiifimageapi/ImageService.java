package fr.didi955.iiifimageapi;

import fr.didi955.iiifimageapi.builder.ImageBuilder;
import fr.didi955.iiifimageapi.entity.Image;
import fr.didi955.iiifimageapi.exception.BadRequestException;
import fr.didi955.iiifimageapi.utils.ImageUtil;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class ImageService {

    @Transactional
    public ResponseEntity<InputStreamResource> getImage(String inventoryNumber, String region, String size, String rotation, String quality, String format) throws ResponseStatusException {
        URL ressourceUrl = getClass().getResource("/images/" + inventoryNumber + ".tif");
        if(ressourceUrl == null) {
            throw new ResponseStatusException(NOT_FOUND, "Unable to find resource");
        }
        try {
            BufferedImage image = ImageIO.read(ressourceUrl);

            try {
                ImageBuilder builder = new ImageBuilder(image);
                builder.region(region).size(size).rotate(rotation).size(size).quality(quality);
                BufferedImage result = builder.build();
                InputStreamResource inputStream = ImageUtil.imageToInputStreamRessource(result, format);

                return ResponseEntity.status(HttpStatus.OK)
                        .contentType(ImageUtil.parseMediaType(format))
                        .body(inputStream);
            }
            catch (IllegalArgumentException e) {
                throw new BadRequestException(e.getMessage());
            }
        }
        catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Transactional
    public Image getImageInfo(String inventoryNumber) throws ResponseStatusException {

        // TODO: Make format file generic

        URL ressourceUrl = getClass().getResource("/images/" + inventoryNumber + ".tif");
        if(ressourceUrl == null) {
            throw new ResponseStatusException(NOT_FOUND, "Unable to find resource");
        }
        try {
            BufferedImage image = ImageIO.read(ressourceUrl);
            return new Image(inventoryNumber, image.getWidth(), image.getHeight(), "tiff");
        }
        catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}
