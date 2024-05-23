package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ScoreTest {
    private Score scoreTest;
    private Score scoreTestB;
    private Score scoreTestC;

    @BeforeEach
    void runBefore() {
        scoreTest = new Score("A", 2019, "00:05:32",
                "String Quartet", 10.31, "", -1);

        scoreTestB = new Score("B", 2000, "00:00:32",
                "Piano Quartet", 8.05, "", -1);

        scoreTestC = new Score("C", 1999, "11:00:00",
                "Piano Concerto", 128.05, "", -1);
    }

    @Test
    void testIsComposedBetween1(){
        assertTrue(scoreTest.isComposedBetween(2018, 2020));
    }

    @Test
    void testIsComposedBetween2(){
        assertTrue(scoreTest.isComposedBetween(2018, 2019));
    }

    @Test
    void testIsComposedBetween3(){
        assertTrue(scoreTest.isComposedBetween(2019, 2020));
    }

    @Test
    void testIsComposedBetween4(){
        assertTrue(scoreTest.isComposedBetween(2019, 2019));
    }

    @Test
    void testIsComposedBetween5(){
        assertFalse(scoreTest.isComposedBetween(1985, 1990));
    }

    @Test
    void testIsComposedBetween6(){
        assertFalse(scoreTest.isComposedBetween(2030, 2042));
    }

    @Test
    void testSumDurationInSecs(){
        assertEquals(332, scoreTest.sumDurationInSecs());
        assertEquals(32, scoreTestB.sumDurationInSecs());
        assertEquals(39600, scoreTestC.sumDurationInSecs());
    }

    @Test
    void testIsDurationBetween1(){
        assertTrue(scoreTest.isDurationBetween(332, 332));
    }

    @Test
    void testIsDurationBetween2(){
        assertTrue(scoreTest.isDurationBetween(331, 332));
    }

    @Test
    void testIsDurationBetween3(){
        assertTrue(scoreTest.isDurationBetween(332, 333));
    }

    @Test
    void testIsDurationBetween4(){
        assertTrue(scoreTest.isDurationBetween(331, 333));
    }

    @Test
    void testIsDurationBetween5(){
        assertFalse(scoreTest.isDurationBetween(221, 225));
    }

    @Test
    void testIsDurationBetween6(){
        assertFalse(scoreTest.isDurationBetween(500, 501));
    }

    @Test
    void testGetTitle(){
        assertEquals("A", scoreTest.getTitle());
    }

    @Test
    void testGetYearComposed(){
        assertEquals(2019, scoreTest.getYearComposed());
    }

    @Test
    void testGetDuration(){
        assertEquals("00:05:32", scoreTest.getDuration());
    }

    @Test
    void testGetInstrumentation(){
        assertEquals("String Quartet", scoreTest.getInstrumentation());
    }

    @Test
    void testGetPrice(){
        assertEquals(10.31, scoreTest.getPrice());
    }

    @Test
    void testGetDifficulty(){
        assertEquals(0, scoreTest.getDifficulty());
    }

    @Test
    void testGetFilePath(){
        assertEquals("", scoreTest.getFilePath());
    }

    @Test
    void testGetFileValue(){
        assertEquals(-1, scoreTest.getFileValue());
    }

    @Test
    void testSetTitle1(){
        scoreTest.setTitle("B");
        assertEquals("B", scoreTest.getTitle());
    }

    @Test
    void testSetTitle2(){
        scoreTest.setTitle("B");
        scoreTest.setTitle("C");
        assertEquals("C", scoreTest.getTitle());
    }

    @Test
    void testSetYearComposed(){
        scoreTest.setYearComposed(2000);
        assertEquals(2000, scoreTest.getYearComposed());
    }

    @Test
    void testSetDuration(){
        scoreTest.setDuration("00:02:00");
        assertEquals("00:02:00", scoreTest.getDuration());
    }

    @Test
    void testSetInstrumentation(){
        scoreTest.setInstrumentation("Solo Piano");
        assertEquals("Solo Piano", scoreTest.getInstrumentation());
    }

    @Test
    void testSetPrice(){
        scoreTest.setPrice(1.02);
        assertEquals(1.02, scoreTest.getPrice());
    }

    @Test
    void testSetDifficulty(){
        scoreTest.setDifficulty(3);
        assertEquals(3, scoreTest.getDifficulty());
    }

    @Test
    void testSetFilePath(){
        scoreTest.setFilePath("1");
        assertEquals("1", scoreTest.getFilePath());
    }

    @Test
    void testSetFileValue(){
        scoreTest.setFileValue(0);
        assertEquals(0, scoreTest.getFileValue());
    }
}

