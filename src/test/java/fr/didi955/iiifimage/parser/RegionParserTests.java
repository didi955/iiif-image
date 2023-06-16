package fr.didi955.iiifimage.parser;

import fr.didi955.iiifimage.image.factory.RegionFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class RegionParserTests {

    @Test
    public void testRegionValid(){
        String region = "full";
        String region2 = "square";

        RegionFactory regionFactory = new RegionFactory(null);

        assertTrue(regionFactory.isRegionValid(region));
        assertTrue(regionFactory.isRegionValid(region2));
    }
}
