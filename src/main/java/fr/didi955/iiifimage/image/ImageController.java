package fr.didi955.iiifimage.image;

import fr.didi955.iiifimage.image.entity.ImageInfo;
import fr.didi955.iiifimage.image.service.ImageService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestController
@RestControllerAdvice
@RequestMapping("/image")
public class ImageController {

    public static Logger LOGGER = LoggerFactory.getLogger(ImageController.class.getName());

    @Autowired
    private ImageService imageService;

    /*
    * Get image endpoint from inventory number and IIIF Image 3.0 specifications
    *
    * @param inventoryNumber
    * @param region
    * @param size
    * @param rotation
    * @param quality
    * @param format
    *
    * @return Image as InputStreamResource
     */
    @GetMapping("/{id}/{region}/{size}/{rotation}/{quality}.{format}")
    public @ResponseBody ResponseEntity<?> getImage(@PathVariable(value = "id") String inventoryNumber,
                                                                                         @PathVariable(value = "region") String region,
                                                                                         @PathVariable(value = "size") String size,
                                                                                         @PathVariable(value = "rotation") String rotation,
                                                                                         @PathVariable(value = "quality") String quality,
                                                                                         @PathVariable(value = "format") String format) {

        return imageService.getImage(inventoryNumber, region, size, rotation, quality, format);
    }

    /*
    * Get image info endpoint from inventory number
    *
    * @param inventoryNumber
    *
    * @return ImageInfo
     */
    @GetMapping("/{inventoryNumber}/info.json")
    public ResponseEntity<?> getImageInfo(@PathVariable(value = "inventoryNumber") String inventoryNumber) {

        ImageInfo imageInfo = imageService.getImageInfo(inventoryNumber);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(imageInfo);
    }

    /*
    * Custom error handler
    *
    * @param req
    * @param e
    *
    * @return ModelAndView with error page and response code
     */
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

    @ExceptionHandler(MissingPathVariableException.class)
    public ModelAndView handleError400(HttpServletRequest req, MissingPathVariableException e) {
        ModelAndView mav = new ModelAndView();
        mav.setStatus(HttpStatus.BAD_REQUEST);
        mav.addObject("status", HttpStatus.BAD_REQUEST.value());
        mav.addObject("message", "Missing path variable: " + e.getVariableName());
        mav.addObject("path", req.getRequestURL());
        mav.setViewName("error");
        return mav;
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView handleError404(HttpServletRequest req, NoHandlerFoundException e) {
        ModelAndView mav = new ModelAndView();
        mav.setStatus(e.getStatusCode());
        mav.addObject("status", e.getStatusCode().value());
        mav.addObject("message", "Invalid URL");
        mav.addObject("path", req.getRequestURL());
        mav.setViewName("error");
        return mav;
    }

}
