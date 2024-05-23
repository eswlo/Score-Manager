package persistence;

import org.json.JSONObject;

/*
code source: JsonSerializationDemo
 */
public interface Writable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}
