package persistence;

import model.*;

import static org.junit.jupiter.api.Assertions.*;

/*
code source: JsonSerializationDemo
 */
public class JsonTest {
    protected void checkScore(String title, int yearComposed, String duration, String instrumentation,
                              double price, String filePath, int fileValue, Score score) {
        assertEquals(title, score.getTitle());
        assertEquals(yearComposed, score.getYearComposed());
        assertEquals(duration, score.getDuration());
        assertEquals(instrumentation, score.getInstrumentation());
        assertEquals(price, score.getPrice());
        assertEquals(filePath, score.getFilePath());
        assertEquals(fileValue, score.getFileValue());
    }
}

