package fr.didi955.iiifimage.image.helpers;

import fr.didi955.iiifimage.image.ImageController;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class ImageHelper {

    public static BufferedImage fetchImage(String inventoryNumber, String imagePath) throws ResponseStatusException {
        // TODO: NEED TO BE GENERIC
        Path path = Paths.get( imagePath + inventoryNumber.replace(".", "_") + ".tif");
        if (Files.exists(path) && !Files.isDirectory(path)){
            try {
                return ImageIO.read(path.toFile());
            } catch (IOException e) {
                ImageController.LOGGER.error(e.getMessage(), e);
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to process image");
            }
        }
        else {
            throw new ResponseStatusException(NOT_FOUND, "Unable to find resource");
        }
    }
}
