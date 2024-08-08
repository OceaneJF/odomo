package odomo;

import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

/**
 * Tests des méthodes de la classe Chauffage.
 */
public class ChauffageTest {

    @Test
    public void testInitialiser() {
        int[][] creneauxTest = {{1, 0}, {1, 0}, {1, 0}, {1, 0}, {1, 0}, {1, 0}, {1, 0}};
        Assert.assertArrayEquals(creneauxTest, creneauxTest);
    }

    @Test
    public void testMatriceCreneaux() {
        Chauffage.creneau1 = new int[][]{{6, 8}, {1, 0}, {1, 0}, {1, 0}, {1, 0}, {1, 0}, {1, 0}};
        Chauffage.creneau2 = new int[][]{{1, 0}, {1, 0}, {1, 0}, {1, 0}, {1, 0}, {1, 0}, {1, 0}};
        boolean[][] matriceTest = new boolean[8][24];
        for (int i = 0; i < matriceTest.length - 1; i++) {
            for (int j = 0; j < matriceTest[0].length - 1; j++) {
                matriceTest[i][j] = i == 0 && (j >= 6 && j <= 8);
            }
        }
        Assert.assertArrayEquals(matriceTest, Chauffage.matriceCreneaux());
    }
}
