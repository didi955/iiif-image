package fr.didi955.iiifimage.image.helpers;

import fr.didi955.iiifimage.exception.ResourceNotFoundException;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ImageHelper {

    public static BufferedImage fetchImage(String inventoryNumber, String imagePath) throws ResponseStatusException, IOException {
        String[] extensions = {".tif", ".tiff", ".TIF", ".TIFF"};
        Path path = null;

        for(String ext : extensions) {
            path = Paths.get(imagePath + inventoryNumber.replace(".", "_") + ext);
            if (Files.exists(path) && !Files.isDirectory(path)) {
                break;
            }
            else {
                path = null;
            }
        }
        if (path != null) {
            return ImageIO.read(path.toFile());
        } else {
            throw new ResourceNotFoundException("Image not found");
        }
    }
}
