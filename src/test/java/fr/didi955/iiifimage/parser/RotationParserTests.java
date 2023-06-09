package fr.didi955.iiifimage.parser;

import fr.didi955.iiifimage.image.factory.RotationFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RotationParserTests {

    @Test
    public void testRotationValid(){
        String rotation = "0";
        String rotation2 = "90";
        String rotation3 = "!90";
        String rotation4 = "180";
        String rotation5 = "!180";
        String rotation6 = "45";
        String rotation7 = "-45";

        assertTrue(RotationFactory.isRotationValid(rotation));
        assertTrue(RotationFactory.isRotationValid(rotation2));
        assertTrue(RotationFactory.isRotationValid(rotation3));
        assertTrue(RotationFactory.isRotationValid(rotation4));
        assertTrue(RotationFactory.isRotationValid(rotation5));
        assertTrue(RotationFactory.isRotationValid(rotation6));
        assertFalse(RotationFactory.isRotationValid(rotation7));
    }
}
