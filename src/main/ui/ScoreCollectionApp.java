package ui;

import model.*;

import java.util.*;

import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;

//import exceptions.*;

//Score Collection application
public class ScoreCollectionApp {
    private static final String JSON_STORE = "./data/ScoreCollection.json";
    private ScoreCollection scoreCollection;
    private Scanner input;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: constructs scoreCollection and runs the application
    public ScoreCollectionApp() throws FileNotFoundException {
        input = new Scanner(System.in);
        scoreCollection = new ScoreCollection("My Score Collection");
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        runScoreCollection();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    @SuppressWarnings("methodlength")
    private void runScoreCollection() {
        boolean keepGoing = true;
        String command = null;
        input = new Scanner(System.in);

        while (keepGoing) {
            displayMenu();
            command = input.next().toLowerCase();

            if (command.equals("q")) {
                System.out.println("Would you like to save your file? (Y/N)");
                String response = input.next().toLowerCase();
                if (response.equals("y")) {
                    if (scoreCollection.getScoreList().size() == 0) {
                        System.out.println("Your current collection is empty. Do you still want to save it?");
                        System.out.println("Press 1 to proceed, or any other keys to return to quit without saving.");
                        String newCommand = null;
                        input = new Scanner(System.in);
                        newCommand = input.next().toLowerCase();
                        if (newCommand.equals("1")) {
                            doSaveScoreCollection();
                            keepGoing = false;
                        } else {
                            System.out.println("File not saved.");
                            keepGoing = false;
                        }
                    } else {
                        doSaveScoreCollection();
                        keepGoing = false;
                    }
                } else if (response.equals("n")) {
                    System.out.println("File not saved.");
                    keepGoing = false;
                } else {
                    System.out.println("Invalid entry. Return to the main menu");
                }
            } else if (command.equals("u")) {
                System.out.print("Enter score title that you'd like to update: ");
                input.nextLine();
                String title = input.nextLine();
                if (!isInCollection(title)) {
                    System.out.println("\"" + title + "\" is NOT in collection!\n");
                } else {
                    Score score = findScoreUsingTitle(title);
                    doUpdateScore(title, score);
                }
            } else {
                processCommand(command);
            }
        }

        System.out.println("\nHave Fun Composing! Ciao!");
    }

    // MODIFIES: this
    // EFFECTS: processes user command
    @SuppressWarnings("methodlength")
    private void processCommand(String command) {
        if (command.equals("a")) {
            doAddScore();
        } else if (command.equals("r")) {
            doRemoveScore();
        } else if (command.equals("v")) {
            doViewScoreDetail();
        } else if (command.equals("p")) {
            doPrintListOfScoreTitles();
        } else if (command.equals("fy")) {
            doFilterByYears();
        } else if (command.equals("fd")) {
            doFilterByDuration();
        } else if (command.equals("s")) {
            if (scoreCollection.getScoreList().size() == 0) {
                System.out.println("Your current collection is empty. Do you still want to save it?");
                System.out.println("Press 1 to proceed, or any other keys to return to the main menu.");
                String newCommand = null;
                input = new Scanner(System.in);
                newCommand = input.next().toLowerCase();
                if (newCommand.equals("1")) {
                    doSaveScoreCollection();
                } else {
                    // do nothing to return to main menu
                }
            } else {
                doSaveScoreCollection();
            }
        } else if (command.equals("l")) {
            doLoadScoreCollection();
        } else {
            System.out.println("Selection not valid...");
        }
    }

    // EFFECTS: displays menu of options to user
    private void displayMenu() {
        System.out.println("\nMain menu");
        System.out.println("Select from:");
        System.out.println("\ta  -> add a new score");
        System.out.println("\tr  -> remove an existing score");
        System.out.println("\tv  -> view detail of a score");
        System.out.println("\tp  -> print list of score titles in collection");
        System.out.println("\tfy -> filter by year");
        System.out.println("\tfd -> filter by duration");
//        System.out.println("\t*  -> rate a score");
        System.out.println("\tu  -> update an existing score");
        System.out.println("\ts  -> save your score collection");
        System.out.println("\tl  -> load your score collection from file");
        System.out.println("\tq  -> quit");
    }

    @SuppressWarnings("methodlength")
    // REQUIRES: numerical values of year, duration, price >= 0
    // MODIFIES: score, scoreCollection
    // EFFECTS: if score is in collection, notify user; otherwise create a new score object,
    // fill in with the entered info, and add it to scoreList in scoreCollection.
    private void doAddScore() {
        System.out.print("Enter score title: ");
        input.nextLine();
        String title = input.nextLine();
        title = title.trim();

        if (isInCollection(title)) {
            System.out.println("\"" + title + "\" is already in collection!\n");
        } else {
            Score newScore = new Score("", 0, "", "", 0.0,
                    "", -1);

            System.out.print("Enter year composed: ");
            int yearComposed = input.nextInt();

            System.out.print("Enter 2 digits for duration of hour (e.g., 01 for 1 hour): ");
            String durHr = input.next();

            System.out.print("Enter 2 digits for duration of minute (e.g., 01 for 1 minute): ");
            String durMin = input.next();

            System.out.print("Enter 2 digits for duration of second (e.g., 01 for 1 second): ");
            String durSec = input.next();
            input.nextLine();

            System.out.print("Enter instrumentation: ");
            String inst = input.nextLine();

            System.out.print("Enter price for sale: ");
            double price = input.nextDouble();

            setterAddScore(newScore, title, yearComposed, durHr + ":" + durMin + ":" + durSec, inst, price);

            if (scoreCollection.addScore(newScore)) {
                System.out.print("\"" + title + "\" was added successfully!\n");
            }
        }
    }

    // MODIFIES: scoreCollection
    // EFFECTS: if score not in collection, notify user; if in collection, remove the score of the given title from
    // scoreCollection's scoreList and notify user, otherwise report removal failed
    private void doRemoveScore() {
        System.out.print("Enter score title to remove: ");
        input.nextLine();
        String removeTitle = input.nextLine();
        if (!isInCollection(removeTitle)) {
            System.out.println("\"" + removeTitle + "\" is NOT in collection!\n");
        } else {
            boolean returnVal = scoreCollection.removeScore(removeTitle);
            if (returnVal) {
                System.out.print("\"" + removeTitle + "\" was removed successfully!\n");
            } else {
                System.out.println("Failed to remove " + "\"" + removeTitle + "\" successfully...\n");
            }
        }
    }

    // EFFECTS: print out the detail of score of given title if score is in collection,
    // otherwise report score not in collection
    private void doViewScoreDetail() {
        System.out.print("Enter score title to view detail: ");
        input.nextLine();
        String viewTitle = input.nextLine();
        if (isInCollection(viewTitle)) {
            Score result = findScoreUsingTitle(viewTitle);
            System.out.println(scoreCollection.viewScoreDetail(result) + "\n");
        } else {
            System.out.println("\"" + viewTitle + "\" is NOT in collection!\n");
        }
    }

    // EFFECTS: if list is empty, notify user accordingly, otherwise print out the
    // list of score titles in the collection
    private void doPrintListOfScoreTitles() {
        if (scoreCollection.viewListOfScoreTitles().size() == 0) {
            System.out.print("Collection is empty!\n");
        } else {
            System.out.println("list of score titles in collection: ");
            System.out.print(scoreCollection.viewListOfScoreTitles() + "\n");
        }
    }

//    // REQUIRES: score title not empty string
//    // MODIFIES: this, score, scoreCollection
//    // EFFECTS: change the score's difficulty level (d). If score is not in collection, notify user.
//    // Otherwise, if d == 0, notify user the undetermined status and ask if update is desired: For entry of Y,
//    // if user enters 0, notify d remains unchanged; if > 5, notify invalid entry; else update d in score
//    // and notify accordingly. For entry of N, notify d remains unchanged.
//    // if d != 0, ask user if update is desired: For entry of N, notify d unchanged; for entry of Y,
//    // if user enters 0, notify d set to undetermined; if > 5, notify invalid entry; else update d in score
//    // and notify accordingly.
//    @SuppressWarnings("methodlength")
//    private void doRateScore() {
//        System.out.print("Enter score title to rate difficulty: \n");
//        String rateTitle = input.next();
//        if (!isInCollection(rateTitle)) {
//            System.out.println("Score is NOT in collection!\n");
//        } else {
//            int index = scoreCollection.viewListOfScoreTitles().indexOf(rateTitle);
//            Score scoreForRating = scoreCollection.getScoreList().get(index);
//
//            if (scoreForRating.getDifficulty() == 0) {
//                System.out.println("Difficulty level undetermined\n");
//                System.out.println("Rate difficulty? (Y/N)\n");
//                String response = input.next();
//                if (response.equals("Y")) {
//                    System.out.print("Enter difficulty level (1 easiest, 5 toughest): ");
//                    int difficulty = input.nextInt();
//                    if (difficulty == 0) {
//                        System.out.println("Difficulty level remains undetermined\n");
//                    } else if (difficulty > 5) {
//                        System.out.print("Invalid entry...\n");
//                    } else {
//                        for (Score next : scoreCollection.getScoreList()) {
//                            if (next.getTitle().equals(rateTitle)) {
//                                next.setDifficulty(difficulty);
//                                System.out.print("score difficulty has been set to level " + difficulty + "!\n");
//                            }
//                        }
//                    }
//                } else {
//                    System.out.println("Difficulty level remains undetermined\n");
//                }
//            } else {
//                System.out.println("Current difficulty level is " + scoreForRating.getDifficulty() + "\n");
//                System.out.println("Change difficulty? (Y/N)\n");
//                String response = input.next();
//                if (response.equals("Y")) {
//                    System.out.print("Enter difficulty level (1 easiest, 5 toughest; or 0 for undetermined): \n");
//                    int difficulty = input.nextInt();
//                    if (difficulty == 0) {
//                        for (Score next : scoreCollection.getScoreList()) {
//                            if (next.getTitle().equals(rateTitle)) {
//                                next.setDifficulty(difficulty);
//                                System.out.print("score difficulty has been set to undetermined.\n");
//                            }
//                        }
//                    } else if (difficulty > 5) {
//                        System.out.print("Invalid entry...\n");
//                    } else {
//                        for (Score next : scoreCollection.getScoreList()) {
//                            if (next.getTitle().equals(rateTitle)) {
//                                next.setDifficulty(difficulty);
//                                System.out.print("score difficulty has been updated to level " + difficulty + "!\n");
//                            }
//                        }
//                    }
//
//                } else {
//                    System.out.println("Difficulty level remains at " + scoreForRating.getDifficulty() + "\n");
//                }
//            }
//        }
//    }


    // EFFECTS: return a list of scores/titles composed between bgn and end (inclusive)
    private void doFilterByYears() {
        System.out.print("Enter beginning range (year): ");
        int bgn = input.nextInt();
        System.out.print("Enter ending range (year): ");
        int end = input.nextInt();
        List<String> returnList = new ArrayList<>();
        for (Score next : scoreCollection.getScoreList()) {
            if (next.isComposedBetween(bgn, end)) {
                returnList.add(next.getTitle() + " (" + next.getYearComposed() + ")");
            }
        }
        System.out.print("List of scores composed between " + bgn + " and " + end + ": " + returnList + "\n");
    }

    // EFFECTS: return a list of scores/titles composed between bgn and end (inclusive)
    private void doFilterByDuration() {
        System.out.print("Enter beginning range (format hh:mm:ss): ");
        String bgn = input.next();
        System.out.print("Enter ending range (format hh:mm:ss): ");
        String end = input.next();
        List<String> returnList = new ArrayList<>();
        for (Score next : scoreCollection.getScoreList()) {
            if (next.isDurationBetween(turnDurStringIntoSecs(bgn), turnDurStringIntoSecs(end))) {
                returnList.add(next.getTitle());
            }
        }
        System.out.print("List of scores with duration between " + bgn + " and " + end + ": " + returnList + "\n");
    }

    // MODIFIES: this
    // EFFECTS: processes user command
    @SuppressWarnings("methodlength")
    private void doUpdateScore(String title, Score score) {
        System.out.println("\nWhat would you like to update?");
        System.out.println("\nSelect from the following, or "
                + "press any other keys to return to the main menu");
        System.out.println("\t1  -> title");
        System.out.println("\t2  -> year of composition");
        System.out.println("\t3  -> duration");
        System.out.println("\t4  -> instrumentation");
        System.out.println("\t5  -> price");
        System.out.println("\t6  -> difficulty");

        String command = null;
        input = new Scanner(System.in);
        command = input.next().toLowerCase();

        if (command.equals("1")) {
            doUpdateScoreTitle(title, score);
        } else if (command.equals("2")) {
            doUpdateScoreYear(title, score);
        } else if (command.equals("3")) {
            doUpdateScoreDuration(title, score);
        } else if (command.equals("4")) {
            doUpdateScoreInstrumentation(title, score);
        } else if (command.equals("5")) {
            doUpdateScorePrice(title, score);
        } else if (command.equals("6")) {
            doUpdateScoreDifficulty(title, score);
        } else {
            //
        }
    }


    // MODIFIES: score
    // EFFECTS: update the score title according to the entered info
    private void doUpdateScoreTitle(String title, Score score) {
        System.out.print("Enter the new title: ");
        input.nextLine();
        String newTitle = input.nextLine();
        score.setTitle(newTitle);
        System.out.print("Score's new title now is " + "\"" + newTitle + "\"" + "!\n");
    }

    // MODIFIES: score
    // EFFECTS: update the score year of composition according to the entered value
    private void doUpdateScoreYear(String title, Score score) {
        System.out.print("Enter the new year when the work was composed: ");
        int newYear = input.nextInt();
        score.setYearComposed(newYear);
        System.out.print("Score's new year of composition now is " + newYear + "!\n");
    }

    // MODIFIES: score
    // EFFECTS: update the score duration according to the entered value
    private void doUpdateScoreDuration(String title, Score score) {
        System.out.print("Enter the new duration (format hh:mm:ss): ");
        input.nextLine();
        String newDur = input.nextLine();
        score.setDuration(newDur);
        System.out.print("Score's new duration now is " + newDur + " (hr:min:sec)!\n");
    }

    // MODIFIES: score
    // EFFECTS: update the score instrumentation according to the entered value
    private void doUpdateScoreInstrumentation(String title, Score score) {
        System.out.print("Enter the new instrumentation: ");
        input.nextLine();
        String newInst = input.nextLine();
        score.setInstrumentation(newInst);
        System.out.print("Score's new instrumentation now is " + newInst + "!\n");
    }

    // MODIFIES: score
    // EFFECTS: update the score price according to the entered value
    private void doUpdateScorePrice(String title, Score score) {
        System.out.print("Enter the new price: ");
        double newPrice = input.nextDouble();
        score.setPrice(newPrice);
        System.out.print("Score's new price now is $" + newPrice + "!\n");
    }

    // MODIFIES: score
    // EFFECTS: update the score difficulty according to the entered value
    private void doUpdateScoreDifficulty(String title, Score score) {
        System.out.print("Enter difficulty level (1 easiest, 5 toughest): ");
        int difficulty = input.nextInt();
        score.setDifficulty(difficulty);
        System.out.print("\"" + title + "\"'s" + " difficulty has been set to level " + difficulty + "!\n");
    }

    // EFFECTS: saves the score collection to file
    private void doSaveScoreCollection() {
        try {
            jsonWriter.open();
            jsonWriter.write(scoreCollection);
            jsonWriter.close();
            System.out.println("Saved " + scoreCollection.getName() + " to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads score collection from file
    private void doLoadScoreCollection() {
        try {
            scoreCollection = jsonReader.read();
            System.out.println("Loaded " + scoreCollection.getName() + " from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    // EFFECTS: return true if score of given title is in collection, otherwise false.
    private boolean isInCollection(String title) {
        Score result = findScoreUsingTitle(title);
        return result != null;
    }

    // REQUIRES: Score/title in Collection
    // EFFECTS: return score found by searching using the title
    private Score findScoreUsingTitle(String title) {
        Score result = null;
        for (Score next : scoreCollection.getScoreList()) {
            if (next.getTitle().equals(title)) {
                result = next;
            }
        }
        return result;
    }

    // EFFECTS: return the duration of dur in seconds
    private int turnDurStringIntoSecs(String dur) {
        String[] parts = dur.split(":");
        List<Integer> listOfDurs = new ArrayList<Integer>();
        for (int i = 0; i < 3; i++) {
            listOfDurs.add(Integer.parseInt(parts[i]));
        }
        int sum = listOfDurs.get(0) * 3600 + listOfDurs.get(1) * 60 + listOfDurs.get(2);
        return sum;

    }

    // REQUIRES: Score in Collection
    // MODIFIES:
    // EFFECTS:
    private String findTitleUsingScore(Score score) {
        String result = "";
        for (Score next : scoreCollection.getScoreList()) {
            if (next == score) {
                result = next.getTitle();
            }
        }
        return result;
    }

    // REQUIRES: numerical values of year, duration, price >= 0
    // MODIFIES: score
    // EFFECTS: set provided details to the given score
    private void setterAddScore(Score newScore, String title, int yearComposed, String dur, String inst, double price) {
        newScore.setTitle(title);
        newScore.setYearComposed(yearComposed);
        newScore.setDuration(dur);
        newScore.setInstrumentation(inst);
        newScore.setPrice(price);
    }

}
