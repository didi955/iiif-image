package fr.didi955.iiifimageapi.service;

import fr.didi955.iiifimageapi.builder.ImageBuilder;
import fr.didi955.iiifimageapi.entity.Image;
import fr.didi955.iiifimageapi.entity.ImageInfo;
import fr.didi955.iiifimageapi.utils.ImageUtil;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class ImageService {

    @Async
    public CompletableFuture<ResponseEntity<InputStreamResource>> getImage(String inventoryNumber, String region, String size, String rotation, String quality, String format) throws ResponseStatusException, InterruptedException {
        URL ressourceUrl = getClass().getResource("/images/" + inventoryNumber + ".tif");
        if(ressourceUrl == null) {
            throw new ResponseStatusException(NOT_FOUND, "Unable to find resource");
        }
        try {
            BufferedImage image = ImageIO.read(ressourceUrl);

            ImageBuilder builder = new ImageBuilder(image);
            builder.region(region).size(size).rotate(rotation).size(size).quality(quality);
            BufferedImage result = builder.build();
            InputStreamResource inputStream = ImageUtil.imageToInputStreamRessource(result, format);

            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.OK)
                    .contentType(ImageUtil.parseMediaType(format))
                    .body(inputStream));
        }
        catch (IOException e) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null));
        }
    }

    @Async
    public CompletableFuture<ImageInfo> getImageInfo(String inventoryNumber) throws ResponseStatusException {

        // TODO: Make format file generic

        URL resourceUrl = getClass().getResource("/images/" + inventoryNumber + ".tif");
        if(resourceUrl == null) {
            throw new ResponseStatusException(NOT_FOUND, "Unable to find resource");
        }
        try {
            BufferedImage resource = ImageIO.read(resourceUrl);
            Image image = new Image(inventoryNumber, resource.getWidth(), resource.getHeight(), "tiff");
            return CompletableFuture.completedFuture(new ImageInfo(image));
        }
        catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to read image");
        }
    }
}
