package fr.didi955.iiifimage.image.entity;

public record Image(String inventoryNumber, int width, int height, String format) {

    @Override
    public String toString() {
        return "Image{" +
                "inventoryNumber='" + inventoryNumber + '\'' +
                ", width='" + width + '\'' +
                ", height='" + height + '\'' +
                '}';
    }
}
