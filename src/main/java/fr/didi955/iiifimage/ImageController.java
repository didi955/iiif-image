package fr.didi955.iiifimage;

import fr.didi955.iiifimage.image.entity.ImageInfo;
import fr.didi955.iiifimage.image.service.ImageService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import java.util.concurrent.ExecutionException;

@RestController
@EnableAsync
@RequestMapping("/image")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @GetMapping("/{id}/{region}/{size}/{rotation}/{quality}.{format}")
    public @ResponseBody ResponseEntity<?> getImage(@PathVariable(value = "id") String inventoryNumber,
                                                                                         @PathVariable(value = "region") String region,
                                                                                         @PathVariable(value = "size") String size,
                                                                                         @PathVariable(value = "rotation") String rotation,
                                                                                         @PathVariable(value = "quality") String quality,
                                                                                         @PathVariable(value = "format") String format){

        try {

            return imageService.getImage(inventoryNumber, region, size, rotation, quality, format).get();

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{inventoryNumber}/info.json")
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

    @ExceptionHandler(ResponseStatusException.class)
    public ModelAndView handleError(HttpServletRequest req, ResponseStatusException e) {
        ModelAndView mav = new ModelAndView();
        mav.setStatus(e.getStatusCode());
        mav.addObject("status", e.getStatusCode().value());
        mav.addObject("message", e.getReason());
        mav.addObject("path", req.getRequestURL());
        mav.setViewName("error");
        return mav;
    }

}
