package fr.didi955.iiifimageapi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.awt.image.BufferedImage;

@Entity
public class Image {

    @Id
    @GeneratedValue
    private long id;

    private String inventoryNumber;
    private int width;
    private int height;

    private String format;

    public Image() {
    }

    public Image(String inventoryNumber, int width, int height, String format) {
        this.inventoryNumber = inventoryNumber;
        this.width = width;
        this.height = height;
        this.format = format;
    }

    public String getFormat() {
        return this.format;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getInventoryNumber() {
        return inventoryNumber;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", inventoryNumber='" + inventoryNumber + '\'' +
                ", width='" + width + '\'' +
                ", height='" + height + '\'' +
                '}';
    }
}
