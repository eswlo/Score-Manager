# My Personal Project: Score Management System

## What will the application do?
This is a score management system for independent/freelancing composers to manage their scores more easily. 
It will allow the user to add scores into their collection, view the entire collection, display the detail of 
each score, and rate the difficulty. 

## Who will use it?
Ideally used by **independent/freelancing composers**

## Why is this project of interest to you?
When I was a composer, I used to spend lots of time organizing my scores, especially when I had to retrieve
information of certain scores for my CV or grant applications. Thus, I thought it'd be interesting to focus on 
solving this problem with my first application/project.


## User Stories
- As a user, I want to be able to add an arbitrary number of musical scores to my collection.
- As a user, I want to be able to view a list of the titles of the scores in my collection
- As a user, I want to be able to select a score in my collection and view it in detail, such as its:
  - year when it was composed
  - instrumentation 
  - duration
  - price 
  - etc.
- As a user, I want to be able to filter the scores in collection by year.
- As a user, I want to be able to filter the scores in collection by duration.
- As a user, I want to be able to update information about each score in the collection, including
  - changing the title, the year of composition, duration, instrumentation, and price;
  - updating/changing the score’s difficulty based on the scale from 1 to 5.
- As a user, I want to be able to remove a score from my collection.
- As a user, I want to have the option of saving my score collection.
  - If the current score collection is empty, I want to be reminded and asked if I wish to proceed.
- As a user, I want to be given the option of loading my score collection from file and resume where I left off last time.
- As a user, when I select the quit option from the application menu, I want to be reminded to save the file, and I want to have the option to do so or not.



# Instructions for Grader (Phase 3)
- You can generate the first required action related to adding Xs to a Y by clicking the Add Score button to add scores into collection.
- You can generate the second required action related to adding Xs to a Y by clicking the Remove Score button to remove scores from collection.
  - You can also click the Filter Collection button and choose filter by year to see qualified scores.
- You can locate my visual component by 
  - linking and clicking the View Score button to view the files,
  - loading the collection to see the gif animation, or 
  - by simply viewing the png icons added to the buttons.
- You can save the state of my application by clicking the Save Collection button.
  - Or you can close the main window without saving the modified collection, and the system will ask whether you want to save what's changed.
- You can reload the state of my application by clicking the Load Collection button.

# Phase 4: Task 2
----- Event Log -----

Sat Nov 25 02:17:35 PST 2023  
Loaded My Score Collection

Sat Nov 25 02:18:03 PST 2023  
Added score: piece3

Sat Nov 25 02:18:28 PST 2023  
Added score: piece4

Sat Nov 25 02:18:42 PST 2023  
Removed score: piece1

Sat Nov 25 02:18:56 PST 2023  
Filtered scores composed between 2000 and 2020

Sat Nov 25 02:19:06 PST 2023  
Linked file to score: piece3

Sat Nov 25 02:19:09 PST 2023  
Saved My Score Collection

Sat Nov 25 02:19:15 PST 2023  
Loaded My Score Collection

# Phase 4: Task 3
- I would probably refactor some common functionalities/features among the UI classes to reduce the duplication, as I may add more relevant UIs later and they sort of share a similar layout. Having a superclass to encapsulate these shared functionalities/features may be of use in terms of enhancing code readability, maintenance, and cohesion, among other things.
- Also, since I will probably only need one instance/window of the CollectionDetailUI class that should be accessed globally, it might be worth considering refactoring it into a singleton class; doing so may offer a more centralized, consistent way for the user to maintain and update its content throughout the runtime of the application.
- Finally, my current approach is to have just 1 score collection, but some users may prefer the option of having sub collections, in which case the composite pattern may be considered. That is, the Score class would be the Leaf, the ScoreCollection class would be the Composite, and a new class (perhaps named something like MusicalWork) would be the Component. However, this would require a certain degree of redesigning to ensure a clean and effective visual presentation of the data.