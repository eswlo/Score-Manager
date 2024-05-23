package ui;

import java.io.FileNotFoundException;

/*
Main class to run the app
 */
public class Main {
    public static void main(String[] args) {
        new ScoreManagementUI();
//        try {
//            new ScoreCollectionApp();
//        } catch (FileNotFoundException e) {
//            System.out.println("Unable to run application: file not found");
//        }
    }
}