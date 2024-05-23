package persistence;

import model.ScoreCollection;
import model.Score;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.json.*;

/*
code source: JsonSerializationDemo
 */
// Represents a reader that reads scoreCollection from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads scoreCollection from file and returns it;
    // throws IOException if an error occurs reading data from file
    public ScoreCollection read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseScoreCollection(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses scoreCollection from JSON object and returns it
    private ScoreCollection parseScoreCollection(JSONObject jsonObject) {
        String name = jsonObject.getString("scoreCollectionName");
        ScoreCollection sc = new ScoreCollection(name);
        addScoreList(sc, jsonObject);
        return sc;
    }

    // MODIFIES: sc
    // EFFECTS: parses scoreList from JSON object and adds them to scoreCollection
    private void addScoreList(ScoreCollection sc, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("scoreList");
        for (Object json : jsonArray) {
            JSONObject nextThingy = (JSONObject) json;
            addScore(sc, nextThingy);
        }
    }

    // MODIFIES: sc
    // EFFECTS: parses score from JSON object and adds it to scoreCollection
    private void addScore(ScoreCollection sc, JSONObject jsonObject) {
        String title = jsonObject.getString("title");
        int yearComposed = jsonObject.getInt("yearComposed");
        String duration = jsonObject.getString("duration");
        String instrumentation = jsonObject.getString("instrumentation");
        double price = jsonObject.getDouble("price");
        int difficulty = jsonObject.getInt("difficulty");
        String filePath = jsonObject.getString("filePath");
        int fileValue = jsonObject.getInt("fileValue");
        Score score = new Score(title, yearComposed, duration, instrumentation, price, filePath, fileValue);
        score.setDifficulty(difficulty);
        sc.addScoreJson(score);
    }
}

