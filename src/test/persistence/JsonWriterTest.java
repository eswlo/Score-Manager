package persistence;

import model.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/*
code source: JsonSerializationDemo
 */
public class JsonWriterTest extends JsonTest {

    @Test
    void testWriterInvalidFile() {
        try {
            ScoreCollection sc = new ScoreCollection("My Score Collection");
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyScoreCollection() {
        try {
            ScoreCollection sc = new ScoreCollection("My Score Collection");
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyScoreCollection.json");
            writer.open();
            writer.write(sc);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyScoreCollection.json");
            sc = reader.read();
            assertEquals("My Score Collection", sc.getName());
            assertEquals(0, sc.getScoreList().size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralScoreCollection() {
        try {
            ScoreCollection sc = new ScoreCollection("My Score Collection");
            Score scoreTestA = new Score("A", 2019, "00:05:32",
                    "String Quartet", 10.31, "", 1);
            Score scoreTestB = new Score("B", 2000, "00:00:32",
                    "Piano Quartet", 8.05, "", 1);

            sc.addScore(scoreTestA);
            sc.addScore(scoreTestB);

            JsonWriter writer = new JsonWriter("./data/testWriterGeneralScoreCollection.json");
            writer.open();
            writer.write(sc);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralScoreCollection.json");
            sc = reader.read();
            assertEquals("My Score Collection", sc.getName());
            List<Score> scoreList = sc.getScoreList();
            assertEquals(2, scoreList.size());
            checkScore("A", 2019, "00:05:32",
                    "String Quartet", 10.31, "", 1, scoreList.get(0));
            checkScore("B", 2000, "00:00:32",
                    "Piano Quartet", 8.05, "", 1, scoreList.get(1));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

}
