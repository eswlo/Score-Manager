package persistence;

import model.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/*
code source: JsonSerializationDemo
 */
public class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            ScoreCollection sc = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyScoreCollection() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyScoreCollection.json");
        try {
            ScoreCollection sc = reader.read();
            assertEquals("My Score Collection", sc.getName());
            assertEquals(0, sc.getScoreList().size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralScoreCollection() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralScoreCollection.json");
        try {
            ScoreCollection sc = reader.read();
            assertEquals("My Score Collection", sc.getName());
            List<Score> scoreList = sc.getScoreList();
            assertEquals(2, scoreList.size());
            checkScore("A", 2019, "00:05:32",
                    "String Quartet", 10.31, "", 1, scoreList.get(0));
            checkScore("B", 2000, "00:00:32",
                    "Piano Quartet", 8.05, "", 1, scoreList.get(1));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

}
