package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ScoreCollectionTest {
    private ScoreCollection scoreCollectionTest;
    private String scoreCollectionName;
    private Score scoreTestA;
    private Score scoreTestB;
    private Score scoreTestC;

    @BeforeEach
    void runBefore() {
        scoreCollectionName = "collectionTest";
        scoreCollectionTest = new ScoreCollection(scoreCollectionName);

        scoreTestA = new Score("A", 2019, "00:05:32",
                "String Quartet", 10.31, "", -1);
        scoreTestB = new Score("B", 2011, "00:17:08",
                "Chamber Ensemble", 25.76, "", -1);
        scoreTestC = new Score("C", 2023, "00:00:05",
                "Miniature for Cello and Piano", 2.10, "", -1);
    }

    @Test
    void testAddScoreEmptyCollection() {
        assertEquals(0, scoreCollectionTest.viewListOfScoreTitles().size());
    }

    @Test
    void testAddScoreAddNewOne() {
        assertTrue(scoreCollectionTest.addScore(scoreTestA));
        assertEquals(1, scoreCollectionTest.viewListOfScoreTitles().size());
        assertEquals("A", scoreCollectionTest.viewListOfScoreTitles().get(0));
    }

    @Test
    void testAddScoreAddSameTwice() {
        assertTrue(scoreCollectionTest.addScore(scoreTestA));
        assertFalse(scoreCollectionTest.addScore(scoreTestA));
    }

    @Test
    void testAddScoreAddTwoDifferent() {
        assertTrue(scoreCollectionTest.addScore(scoreTestB));
        assertTrue(scoreCollectionTest.addScore(scoreTestC));
        assertEquals(2, scoreCollectionTest.viewListOfScoreTitles().size());
        assertEquals("B", scoreCollectionTest.viewListOfScoreTitles().get(0));
        assertEquals("C", scoreCollectionTest.viewListOfScoreTitles().get(1));
    }

    @Test
    void testAddScoreAddThreeDifferent() {
        assertTrue(scoreCollectionTest.addScore(scoreTestB));
        assertTrue(scoreCollectionTest.addScore(scoreTestC));
        assertTrue(scoreCollectionTest.addScore(scoreTestA));
        assertEquals(3, scoreCollectionTest.viewListOfScoreTitles().size());
        assertEquals("B", scoreCollectionTest.viewListOfScoreTitles().get(0));
        assertEquals("C", scoreCollectionTest.viewListOfScoreTitles().get(1));
        assertEquals("A", scoreCollectionTest.viewListOfScoreTitles().get(2));
        assertFalse(scoreCollectionTest.addScore(scoreTestB));
    }

    @Test
    void testRemoveScoreNotInCollection() {
        assertFalse(scoreCollectionTest.removeScore(scoreTestA.getTitle()));
    }

    @Test
    void testRemoveScoreInCollection1() {
        scoreCollectionTest.addScore(scoreTestA);
        assertTrue(scoreCollectionTest.removeScore(scoreTestA.getTitle()));
        assertEquals(0, scoreCollectionTest.viewListOfScoreTitles().size());
        assertFalse(scoreCollectionTest.removeScore(scoreTestA.getTitle()));
    }

    @Test
    void testRemoveScoreInCollection2() {
        Score scoreTestD = new Score("D", 1999, "00:02:00", "Piano Trio",
                3.06, "", 1);
        scoreCollectionTest.addScore(scoreTestA);
        scoreCollectionTest.addScore(scoreTestB);
        scoreCollectionTest.addScore(scoreTestC);
        assertTrue(scoreCollectionTest.removeScore(scoreTestB.getTitle()));
        assertFalse(scoreCollectionTest.removeScore(scoreTestD.getTitle()));
        assertEquals(2, scoreCollectionTest.viewListOfScoreTitles().size());
        assertEquals("A", scoreCollectionTest.viewListOfScoreTitles().get(0));
        assertEquals("C", scoreCollectionTest.viewListOfScoreTitles().get(1));
    }

    @Test
    void testAddRemoveAddScoreInCollection() {
        Score scoreTestD = new Score("D", 1999, "00:02:00", "Piano Trio",
                3.06, "", 1);
        scoreCollectionTest.addScore(scoreTestA);
        scoreCollectionTest.addScore(scoreTestB);
        scoreCollectionTest.addScore(scoreTestC);
        assertTrue(scoreCollectionTest.removeScore(scoreTestB.getTitle()));
        assertFalse(scoreCollectionTest.removeScore(scoreTestD.getTitle()));
        assertEquals(2, scoreCollectionTest.viewListOfScoreTitles().size());
        assertEquals("A", scoreCollectionTest.viewListOfScoreTitles().get(0));
        assertEquals("C", scoreCollectionTest.viewListOfScoreTitles().get(1));
        scoreCollectionTest.addScore(scoreTestD);
        assertEquals(3, scoreCollectionTest.viewListOfScoreTitles().size());
        assertEquals("D", scoreCollectionTest.viewListOfScoreTitles().get(2));
    }

    @Test
    void testViewScoreDetailNotInCollection() {
        assertEquals(scoreTestB.getTitle() + " not in collection!",
                scoreCollectionTest.viewScoreDetail(scoreTestB));
    }

    @Test
    void testViewScoreDetailScoreInCollection1() {
        scoreCollectionTest.addScore(scoreTestA);
        String expectedDetailA = "Title: A; Year: 2019; Duration: 00:05:32 (hr:min:sec); " +
                "Instrumentation: String Quartet; " + "Price: $10.31; Difficulty: undetermined";
        assertEquals(expectedDetailA, scoreCollectionTest.viewScoreDetail(scoreTestA));
    }

    @Test
    void testViewScoreDetailScoreInCollection2() {
        scoreCollectionTest.addScore(scoreTestA);
        scoreTestA.setDifficulty(2);
        String expectedDetailA = "Title: A; Year: 2019; Duration: 00:05:32 (hr:min:sec); " +
                "Instrumentation: String Quartet; " + "Price: $10.31; Difficulty: 2";
        assertEquals(expectedDetailA, scoreCollectionTest.viewScoreDetail(scoreTestA));
    }

    @Test
    void testViewScoreDetailScoreInCollection3() {
        scoreCollectionTest.addScore(scoreTestA);
        scoreCollectionTest.addScore(scoreTestB);
        scoreCollectionTest.addScore(scoreTestC);

        String expectedDetailA = "Title: A; Year: 2019; Duration: 00:05:32 (hr:min:sec); " +
                "Instrumentation: String Quartet; " +
                "Price: $10.31; Difficulty: undetermined";
        assertEquals(expectedDetailA, scoreCollectionTest.viewScoreDetail(scoreTestA));

        String expectedDetailC = "Title: C; Year: 2023; Duration: 00:00:05 (hr:min:sec); " +
                "Instrumentation: Miniature for Cello and Piano; " +
                "Price: $2.1; Difficulty: undetermined";
        assertEquals(expectedDetailC, scoreCollectionTest.viewScoreDetail(scoreTestC));
    }

    @Test
    void testViewScoreDetailScoreInCollection4() {
        scoreCollectionTest.addScore(scoreTestA);
        scoreCollectionTest.addScore(scoreTestB);
        scoreCollectionTest.addScore(scoreTestC);

        scoreTestA.setDifficulty(2);
        scoreTestC.setDifficulty(5);

        String expectedDetailA = "Title: A; Year: 2019; Duration: 00:05:32 (hr:min:sec); " +
                "Instrumentation: String Quartet; " +
                "Price: $10.31; Difficulty: 2";
        assertEquals(expectedDetailA, scoreCollectionTest.viewScoreDetail(scoreTestA));

        String expectedDetailB = "Title: B; Year: 2011; Duration: 00:17:08 (hr:min:sec); " +
                "Instrumentation: Chamber Ensemble; " +
                "Price: $25.76; Difficulty: undetermined";
        assertEquals(expectedDetailB, scoreCollectionTest.viewScoreDetail(scoreTestB));

        String expectedDetailC = "Title: C; Year: 2023; Duration: 00:00:05 (hr:min:sec); " +
                "Instrumentation: Miniature for Cello and Piano; " +
                "Price: $2.1; Difficulty: 5";
        assertEquals(expectedDetailC, scoreCollectionTest.viewScoreDetail(scoreTestC));
    }

    @Test
    void testViewListOfScoreTitlesEmptyCollection() {
        assertEquals(0, scoreCollectionTest.viewListOfScoreTitles().size());
    }

    @Test
    void testViewListOfScoreTitlesOneScore() {
        scoreCollectionTest.addScore(scoreTestA);
        assertEquals("A", scoreCollectionTest.viewListOfScoreTitles().get(0));
    }

    @Test
    void testViewListOfScoreTitlesMultipleScores() {
        scoreCollectionTest.addScore(scoreTestA);
        scoreCollectionTest.addScore(scoreTestB);
        scoreCollectionTest.addScore(scoreTestC);
        assertEquals(3, scoreCollectionTest.viewListOfScoreTitles().size());
        assertEquals("A", scoreCollectionTest.viewListOfScoreTitles().get(0));
        assertEquals("B", scoreCollectionTest.viewListOfScoreTitles().get(1));
        assertEquals("C", scoreCollectionTest.viewListOfScoreTitles().get(2));
    }

    @Test
    void testRateScore() {
        scoreCollectionTest.addScore(scoreTestA);
        scoreCollectionTest.addScore(scoreTestB);
        scoreCollectionTest.addScore(scoreTestC);

        scoreCollectionTest.rateScore("A", 5);
        assertEquals(5, scoreTestA.getDifficulty());

        scoreCollectionTest.rateScore("B", 1);
        assertEquals(1, scoreTestB.getDifficulty());
    }

    @Test
    void testGetScoreList() {
        scoreCollectionTest.addScore(scoreTestA);
        scoreCollectionTest.addScore(scoreTestB);
        scoreCollectionTest.addScore(scoreTestC);

        assertEquals(scoreTestA, scoreCollectionTest.getScoreList().get(0));
        assertEquals(scoreTestB, scoreCollectionTest.getScoreList().get(1));
    }

    @Test
    void testGetTitle(){
        assertEquals("A", scoreTestA.getTitle());
    }

    @Test
    void testGetYearComposed(){
        assertEquals(2019, scoreTestA.getYearComposed());
    }

    @Test
    void testGetDuration(){
        assertEquals("00:05:32", scoreTestA.getDuration());
    }

    @Test
    void testGetInstrumentation(){
        assertEquals("String Quartet", scoreTestA.getInstrumentation());
    }

    @Test
    void testGetPrice(){
        assertEquals(10.31, scoreTestA.getPrice());
    }

    @Test
    void testGetDifficulty(){
        assertEquals(0, scoreTestA.getDifficulty());
    }

    @Test
    void testSetTitle1(){
        scoreTestA.setTitle("B");
        assertEquals("B", scoreTestA.getTitle());
    }

    @Test
    void testSetTitle2(){
        scoreTestA.setTitle("B");
        scoreTestA.setTitle("C");
        assertEquals("C", scoreTestA.getTitle());
    }

    @Test
    void testSetYearComposed(){
        scoreTestA.setYearComposed(2000);
        assertEquals(2000, scoreTestA.getYearComposed());
    }

    @Test
    void testSetDuration(){
        scoreTestA.setDuration("2");
        assertEquals("2", scoreTestA.getDuration());
    }

    @Test
    void testSetInstrumentation(){
        scoreTestA.setInstrumentation("Solo Piano");
        assertEquals("Solo Piano", scoreTestA.getInstrumentation());
    }

    @Test
    void testSetPrice(){
        scoreTestA.setPrice(1.02);
        assertEquals(1.02, scoreTestA.getPrice());
    }

    @Test
    void testSetDifficulty(){
        scoreTestA.setDifficulty(3);
        assertEquals(3, scoreTestA.getDifficulty());
    }



}
