package fr.didi955.iiifimageapi;

import fr.didi955.iiifimageapi.entity.ImageInfo;
import fr.didi955.iiifimageapi.exception.BadRequestException;
import fr.didi955.iiifimageapi.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@EnableAsync
@RequestMapping("/iiif")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @GetMapping("/image/{id}/{region}/{size}/{rotation}/{quality}.{format}")
    public @ResponseBody ResponseEntity<?> getImage(@PathVariable(value = "id") String inventoryNumber,
                                                                                         @PathVariable(value = "region") String region,
                                                                                         @PathVariable(value = "size") String size,
                                                                                         @PathVariable(value = "rotation") String rotation,
                                                                                         @PathVariable(value = "quality") String quality,
                                                                                         @PathVariable(value = "format") String format){

        try {
            return imageService.getImage(inventoryNumber, region, size, rotation, quality, format).get();
        }
        catch (BadRequestException e) {
            return e.sendResponse();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/image/{inventoryNumber}/info.json")
    public ResponseEntity<?> getImageInfo(@PathVariable(value = "inventoryNumber") String inventoryNumber) {

        try {
            ImageInfo imageInfo = imageService.getImageInfo(inventoryNumber).get();

            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(imageInfo);
        }
        catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
