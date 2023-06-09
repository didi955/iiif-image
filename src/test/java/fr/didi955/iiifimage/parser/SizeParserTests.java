package fr.didi955.iiifimage.parser;

import fr.didi955.iiifimage.image.factory.SizeFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SizeParserTests {

    @Test
    public void testScaledWidth() {
        String size = "100,";
        assertTrue(SizeFactory.isScaledWidth(size));
    }

    @Test
    public void testScaledWidthUpscale() {
        String size = "^100,";
        String size2 = "^550,";
        String size3 = "100,50";
        String size4 = "100,";
        assertTrue(SizeFactory.isScaledWidthUpscale(size));
        assertTrue(SizeFactory.isScaledWidthUpscale(size2));
        assertFalse(SizeFactory.isScaledWidthUpscale(size3));
        assertFalse(SizeFactory.isScaledWidthUpscale(size4));
    }

    @Test
    public void testScaledHeight() {
        String size = ",100";
        String size2 = ",!100";
        String size3 = "!100,";
        String size4 = "!100,!100";
        assertTrue(SizeFactory.isScaledHeight(size));
        assertFalse(SizeFactory.isScaledHeight(size2));
        assertFalse(SizeFactory.isScaledHeight(size3));
        assertFalse(SizeFactory.isScaledHeight(size4));
    }

    @Test
    public void testScaledHeightUpscale() {
        String size = "^,100";
        String size2 = "^,500";
        String size3 = "!100,";
        assertTrue(SizeFactory.isScaledHeightUpscale(size));
        assertTrue(SizeFactory.isScaledHeightUpscale(size2));
        assertFalse(SizeFactory.isScaledHeightUpscale(size3));
    }

    @Test
    public void testScaledPercentage() {
        String size = "pct:100";
        String size2 = "pct:0";
        String size3 = "pct:101";
        String size4 = "pct:50";
        String size5 = "50";
        assertTrue(SizeFactory.isScaledPercentage(size));
        assertTrue(SizeFactory.isScaledPercentage(size4));
        assertFalse(SizeFactory.isScaledPercentage(size2));
        assertFalse(SizeFactory.isScaledPercentage(size3));
        assertFalse(SizeFactory.isScaledPercentage(size5));
    }

    @Test
    public void testScaledPercentageUpscale() {
        String size = "^pct:100";
        String size2 = "^pct:0";
        String size3 = "^pct:101";
        String size4 = "^pct:50";
        String size5 = "^50";
        assertTrue(SizeFactory.isScaledPercentageUpscale(size));
        assertTrue(SizeFactory.isScaledPercentageUpscale(size4));
        assertFalse(SizeFactory.isScaledPercentageUpscale(size2));
        assertFalse(SizeFactory.isScaledPercentageUpscale(size3));
        assertFalse(SizeFactory.isScaledPercentageUpscale(size5));
    }

    @Test
    public void testExactWidthHeight(){
        String size = "100,100";
        String size2 = "80,50";
        String size3 = "850,782";
        String size4 = "^850,";
        String size5 = ",999";
        String size6 = "587,!85";
        assertTrue(SizeFactory.isExactWidthHeight(size));
        assertTrue(SizeFactory.isExactWidthHeight(size2));
        assertTrue(SizeFactory.isExactWidthHeight(size3));
        assertFalse(SizeFactory.isExactWidthHeight(size4));
        assertFalse(SizeFactory.isExactWidthHeight(size5));
        assertFalse(SizeFactory.isExactWidthHeight(size6));
    }

    @Test
    public void testExactWidthHeightUpscale(){
        String size = "^100,100";
        String size2 = "^80,50";
        String size3 = "^850,782";
        String size4 = "^850,";
        String size5 = "^,999";
        String size6 = "^587,!85";
        assertTrue(SizeFactory.isExactWidthHeightUpscale(size));
        assertTrue(SizeFactory.isExactWidthHeightUpscale(size2));
        assertTrue(SizeFactory.isExactWidthHeightUpscale(size3));
        assertFalse(SizeFactory.isExactWidthHeightUpscale(size4));
        assertFalse(SizeFactory.isExactWidthHeightUpscale(size5));
        assertFalse(SizeFactory.isExactWidthHeightUpscale(size6));
    }

    @Test
    public void testScaledWidthHeight(){
        String size = "100,";
        String size2 = ",100";
        String size3 = "pct:100";
        String size4 = "100,100";
        String size5 = "100,100";
        String size6 = "100,100";
        String size7 = "^100,100";
        String size8 = "!50,100";
        assertFalse(SizeFactory.isScaledWidthHeight(size));
        assertFalse(SizeFactory.isScaledWidthHeight(size2));
        assertFalse(SizeFactory.isScaledWidthHeight(size3));
        assertFalse(SizeFactory.isScaledWidthHeight(size4));
        assertFalse(SizeFactory.isScaledWidthHeight(size5));
        assertFalse(SizeFactory.isScaledWidthHeight(size6));
        assertFalse(SizeFactory.isScaledWidthHeight(size7));
        assertTrue(SizeFactory.isScaledWidthHeight(size8));
    }

    @Test
    public void testScaledWidthHeightUpscale(){
        String size = "^100,";
        String size2 = "^,100";
        String size3 = "^pct:100";
        String size4 = "^100,100";
        String size5 = "^100,100";
        String size6 = "^100,100";
        String size7 = "^100,100";
        String size8 = "^!50,100";
        assertFalse(SizeFactory.isScaledWidthHeightUpscale(size));
        assertFalse(SizeFactory.isScaledWidthHeightUpscale(size2));
        assertFalse(SizeFactory.isScaledWidthHeightUpscale(size3));
        assertFalse(SizeFactory.isScaledWidthHeightUpscale(size4));
        assertFalse(SizeFactory.isScaledWidthHeightUpscale(size5));
        assertFalse(SizeFactory.isScaledWidthHeightUpscale(size6));
        assertFalse(SizeFactory.isScaledWidthHeightUpscale(size7));
        assertTrue(SizeFactory.isScaledWidthHeightUpscale(size8));
    }




}
