package model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import persistence.Writable;

import javax.swing.*;

/*
Represent a score that has a title, year composed, duration (in format hh:mm:ss), instrumentation, price (in $),
and difficulty (from 1 to 5, but initially set to 0)
*/
public class Score implements Writable {
    private String title;
    private int yearComposed;
    private String duration;
    private String instrumentation;
    private double price;
    private int difficulty;
    private String filePath;
    private int fileValue;

    // REQUIRES: year > 0; numerical value of duration can't be negative; price >= 0.0;
    // EFFECTS: construct a score with a title, year composed, duration (format hr:min:sec), instrumentation,
    // price for sale (in $), with initial difficulty set to 0, filePath, and fileValue.
    public Score(String title, int yearComposed, String duration, String instrumentation, double price,
                 String filePath, int fileValue) {
        this.title = title;
        this.yearComposed = yearComposed;
        this.duration = duration;
        this.instrumentation = instrumentation;
        this.price = price;
        this.difficulty = 0;
        this.filePath = filePath;
        this.fileValue = fileValue;
    }

    //REQUIRES: bgn <= end, both >= 0
    //EFFECTS: return true if score is composed between bgn and end (inclusive),
    // otherwise return false
    public boolean isComposedBetween(int bgn, int end) {
        if (bgn <= this.getYearComposed() && this.getYearComposed() <= end) {
            return true;
        } else {
            return false;
        }
    }

    //REQUIRES: bgn <= end; both >= 0
    //EFFECTS: return true if score's duration is between bgn and end (inclusive),
    // otherwise return false
    public boolean isDurationBetween(int bgn, int end) {
        if (bgn <= this.sumDurationInSecs() && this.sumDurationInSecs() <= end) {
            return true;
        } else {
            return false;
        }
    }

    //EFFECTS: return the score's duration in seconds
    public int sumDurationInSecs() {
        String[] parts = this.duration.split(":");
        List<Integer> listOfDurs = new ArrayList<Integer>();

        for (int i = 0; i < 3; i++) {
            listOfDurs.add(Integer.parseInt(parts[i]));
        }

        int sum = listOfDurs.get(0) * 3600 + listOfDurs.get(1) * 60 + listOfDurs.get(2);

        return sum;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setYearComposed(int yearComposed) {
        this.yearComposed = yearComposed;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setInstrumentation(String instrumentation) {
        this.instrumentation = instrumentation;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setFileValue(int fileValue) {
        this.fileValue = fileValue;
        if (fileValue == JFileChooser.APPROVE_OPTION) {
            EventLog.getInstance().logEvent(new Event("Linked file to score: " + this.getTitle()));
        }
    }

    public String getTitle() {
        return this.title;
    }

    public int getYearComposed() {
        return this.yearComposed;
    }

    public String getDuration() {
        return this.duration;
    }

    public String getInstrumentation() {
        return this.instrumentation;
    }

    public double getPrice() {
        return this.price;
    }

    public int getDifficulty() {
        return this.difficulty;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public int getFileValue() {
        return this.fileValue;
    }

    @Override
    //EFFECTS:put score fields in JSONObject
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("title", title);
        json.put("yearComposed", yearComposed);
        json.put("duration", duration);
        json.put("instrumentation", instrumentation);
        json.put("price", price);
        json.put("difficulty", difficulty);
        json.put("filePath", filePath);
        json.put("fileValue", fileValue);
        return json;
    }

}
