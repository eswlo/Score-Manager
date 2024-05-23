package model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.json.JSONObject;
import org.json.JSONArray;
import persistence.Writable;
import persistence.*;

/*
Represent a score collection with a name and a list of scores added into the collection
 */
public class ScoreCollection implements Writable {
    private String scoreCollectionName;
    private List<Score> scoreList;
//    private List<Score> scoreRevisionList;

    // EFFECTS: construct a scoreCollection with a name and an empty list for scores
    public ScoreCollection(String scoreCollectionName) {
        this.scoreCollectionName = scoreCollectionName;
        this.scoreList = new ArrayList<>();
//        this.scoreRevisionList = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: if score is already in the collection, return false,
    // otherwise add it into scoreList and return true
    public boolean addScore(Score score) {
        if (isAdded(score)) {
            return false;
        } else {
            this.scoreList.add(score);
            EventLog.getInstance().logEvent(new Event("Added score: " + score.getTitle()));
            return true;
        }
    }

    // MODIFIES: this
    // EFFECTS: for json; if score is already in the collection, return false,
    // otherwise add it into scoreList and return true
    public boolean addScoreJson(Score score) {
        if (isAdded(score)) {
            return false;
        } else {
            this.scoreList.add(score);
            return true;
        }
    }

    // MODIFIES: this
    // EFFECTS: if score is not in the collection, return false,
    // otherwise find and remove it from scoreList and return true;
    public boolean removeScore(String scoreTitle) {
        for (int i = 0; i < this.scoreList.size(); i++) {
            if (this.scoreList.get(i).getTitle().equals(scoreTitle)) {
                this.scoreList.remove(i);
                EventLog.getInstance().logEvent(new Event("Removed score: " + scoreTitle));
                return true;
            }
        }
        return false;
    }


    // EFFECTS: if score is not in the collection, return score.getTitle() + " not in collection!",
    // otherwise retrieve the detail and return as a string
    public String viewScoreDetail(Score score) {
        if (!isAdded(score)) {
            return score.getTitle() + " not in collection!";
        } else {
            if (score.getDifficulty() == 0) {
                String scoreDetail = "Title: " + score.getTitle() + "; "
                        + "Year: " + score.getYearComposed() + "; "
                        + "Duration: " + score.getDuration() + " (hr:min:sec); "
                        + "Instrumentation: " + score.getInstrumentation() + "; "
                        + "Price: $" + score.getPrice() + "; "
                        + "Difficulty: " + "undetermined";
                return scoreDetail;
            } else {
                String scoreDetail = "Title: " + score.getTitle() + "; "
                        + "Year: " + score.getYearComposed() + "; "
                        + "Duration: " + score.getDuration() + " (hr:min:sec); "
                        + "Instrumentation: " + score.getInstrumentation() + "; "
                        + "Price: $" + score.getPrice() + "; "
                        + "Difficulty: " + score.getDifficulty();
                return scoreDetail;
            }
        }
    }

    // EFFECTS: if scoreList is empty, return empty list,
    // otherwise return a list of score titles in the order of being added in
    public List<String> viewListOfScoreTitles() {
        List<String> returnList = new ArrayList<>();
        if (this.scoreList.size() == 0) {
            returnList = returnList;
        } else {
            for (Score next : this.scoreList) {
                returnList.add(next.getTitle());
            }
        }
        return returnList;
    }

    // REQUIRES: Score already in collection
    // MODIFIES: Score
    // EFFECTS: assign int value from 1 to 5 (least to most difficult) to the given score
    public void rateScore(String scoreTitle, int difficulty) {
        for (Score next : this.scoreList) {
            if (next.getTitle().equals(scoreTitle)) {
                next.setDifficulty(difficulty);
            }
        }
    }

//    // REQUIRES: score in collection
//    // MODIFIES: this
//    // EFFECTS: remove the score with given title from scoreList, and it into scoreRevisionList
//    // and return scoreRevisionList
//    public List<Score> addToScoreRevisionList(String title) {
//        for (Score next : this.scoreList) {
//            if (next.getTitle().equals(title)) {
//                int index = this.scoreList.indexOf(next);
//                Score removed = this.scoreList.remove(index);
//                this.scoreRevisionList.add(removed);
//            }
//        }
//        return scoreRevisionList;
//    }


//    // MODIFIES: this
//    // EFFECTS: change/update the collection name
//    public void updateCollectionName(String newCollectionName) {
//        this.scoreCollectionName = newCollectionName;
//    }

    // EFFECTS: if score in scoreList, return true; otherwise return false
    private boolean isAdded(Score score) {
        return this.scoreList.contains(score);
    }

    public List<Score> getScoreList() {
        return this.scoreList;
    }

    public String getName() {
        return scoreCollectionName;
    }

    // MODIFIES: this
    // EFFECTS: save score collection to Json file; return true if successful, or false if exception is caught
    public boolean saveToJson(JsonWriter jsonWriter) {
        try {
            jsonWriter.open();
            jsonWriter.write(this);
            jsonWriter.close();
            EventLog.getInstance().logEvent(new Event("Saved " + this.scoreCollectionName));
            return true;
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    // EFFECTS: load score collection from Json file and return
    public ScoreCollection loadFromJson(JsonReader jsonReader) throws IOException {
        ScoreCollection result = jsonReader.read();
        EventLog.getInstance().logEvent(new Event("Loaded " + this.scoreCollectionName));
        return result;
    }

    // EFFECTS: filter and return score list by year
    public List<Score> filterScoresByYear(int bgn, int end) {
        List<Score> returnList = new ArrayList<>();
        for (Score next : this.getScoreList()) {
            if (next.isComposedBetween(bgn, end)) {
                returnList.add(next);
            }
        }
        EventLog.getInstance().logEvent(new Event("Filtered scores composed between " + bgn + " and " + end));
        return returnList;
    }

    @Override
    //EFFECTS:put score collection fields in JSONObject
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("scoreCollectionName", scoreCollectionName);
        json.put("scoreList", scoreListToJson());
        return json;
    }

    // EFFECTS: returns scorelist in this scoreCollection as a JSON array
    private JSONArray scoreListToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Score s : scoreList) {
            jsonArray.put(s.toJson());
        }

        return jsonArray;
    }

    // EFFECTS: call another overloading method quicksort and return the result
    public List<Score> quicksortYear(List<Score> arrayList) {
        Score[] array = arrayList.toArray(new Score[arrayList.size()]);
        return quicksortYear(array, 0, array.length - 1);
    }

    // EFFECTS: sort and return the score list
    private List<Score> quicksortYear(Score[] array, int lowIndex, int highIndex) {
        if (lowIndex < highIndex) {
            int pivotIndex = new Random().nextInt(highIndex - lowIndex) + lowIndex;
            int pivot = array[pivotIndex].getYearComposed();
            swap(array, pivotIndex, highIndex);

            int leftPointer = lowIndex;
            int rightPointer = highIndex;

            while (leftPointer < rightPointer) {
                while (array[leftPointer].getYearComposed() <= pivot && leftPointer < rightPointer) {
                    leftPointer++;
                }
                while (array[rightPointer].getYearComposed() >= pivot && leftPointer < rightPointer) {
                    rightPointer--;
                }
                swap(array, leftPointer, rightPointer);
            }

            swap(array, leftPointer, highIndex);
            quicksortYear(array, lowIndex, leftPointer - 1);
            quicksortYear(array, leftPointer + 1, highIndex);
        }

        List<Score> arrayList = new ArrayList<>(Arrays.asList(array));
        return arrayList;

    }

    // EFFECTS: exchange elements in array
    private void swap(Score[] array, int index1, int index2) {
        Score temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
    }



}
