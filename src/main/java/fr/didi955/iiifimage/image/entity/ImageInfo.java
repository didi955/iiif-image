package fr.didi955.iiifimage.image.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ImageInfo {

    public static final String CONTEXT = "http://iiif.io/api/image/3/context.json";
    public static final String TYPE = "ImageService3";
    public static final String PROTOCOL = "http://iiif.io/api/image";
    public static final String PROFILE = "level2";

    @JsonIgnore
    private Image image;

    public ImageInfo() {
    }

    public ImageInfo(Image image) {
        this.image = image;
    }

    @JsonProperty("@context")
    public String getContext() {
        return CONTEXT;
    }

    // TODO: Make this generic
    @JsonProperty("@id")
    public String getId() {
        // TODO: Make this generic
        return "http://localhost:8080/iiif/image/" + image.inventoryNumber();
    }

    @JsonProperty("@type")
    public String getType() {
        return TYPE;
    }

    @JsonProperty("protocol")
    public String getProtocol() {
        return PROTOCOL;
    }

    @JsonProperty("profile")
    public String getProfile() {
        return PROFILE;
    }

    @JsonProperty("width")
    public int getWidth() {
        return image.width();
    }

    @JsonProperty("height")
    public int getHeight() {
        return image.height();
    }

    @JsonProperty("maxWidth")
    public int getMaxWidth() {
        return image.width();
    }

    @JsonProperty("maxHeight")
    public int getMaxHeight() {
        return image.height();
    }

    public Image getImage() {
        return image;
    }
}
