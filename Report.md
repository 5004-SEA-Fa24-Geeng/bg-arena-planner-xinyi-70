# Report

Submitted report to be manually graded. We encourage you to review the report as you read through the provided
code as it is meant to help you understand some of the concepts. 

## Technical Questions

1. What is the difference between == and .equals in java? Provide a code example of each, where they would return different results for an object. Include the code snippet using the hash marks (```) to create a code block.

   `==` compares object references (memory addresses) to see if two references point to the exact same object.
   `.equals()` compares the content or values of objects.
   ```java
   // your code here
   BoardGame game1 = new BoardGame("Chess", 1, 2, 4, 30, 60, 3.2, 500, 8.5, 2000);
   BoardGame game2 = new BoardGame("Chess", 2, 2, 4, 30, 60, 3.2, 500, 8.5, 2000);
   boolean referenceEqual = (game1 == game2); // false
   // Assuming BoardGame properly overrides equals() to compare by name
   boolean contentEqual = game1.equals(game2); // true
   ```


2. Logical sorting can be difficult when talking about case. For example, should "apple" come before "Banana" or after? How would you sort a list of strings in a case-insensitive manner?

   We can convert strings to lowercase (or uppercase) before comparison. This ensures that the sorting is based solely on the alphabetical order. With this approach, "apple" would correctly be sorted before "Banana" in alphabetical order, maintaining logical alphabetical sequencing regardless of case.


3. In our version of the solution, we had the following code (snippet)
    ```java
    public static Operations getOperatorFromStr(String str) {
        if (str.contains(">=")) {
            return Operations.GREATER_THAN_EQUALS;
        } else if (str.contains("<=")) {
            return Operations.LESS_THAN_EQUALS;
        } else if (str.contains(">")) {
            return Operations.GREATER_THAN;
        } else if (str.contains("<")) {
            return Operations.LESS_THAN;
        } else if (str.contains("=="))...
    ```
    Why would the order in which we checked matter (if it does matter)? Provide examples either way proving your point. 

   The order of checking operators in this code matters significantly because of how string containment works. When checking for substring presence with contains(), the method simply checks if the characters appear somewhere in the string. This order matters particularly for operators that are substrings of other operators.
   Examples:
    ```java
   javaCopypublic static Operations getOperatorFromStr(String str) {
   if (str.contains(">")) {
   return Operations.GREATER_THAN;
   } else if (str.contains(">=")) {
   return Operations.GREATER_THAN_EQUALS;
   }
   }
    ```
   For input "minPlayers>=2", it will incorrectly return GREATER_THAN because ">" is found first, and the code never reaches the check for ">=".


4. What is the difference between a List and a Set in Java? When would you use one over the other?
   
   List is an ordered collection that allows duplicates and provides index-based access. Set is a collection that ensures uniqueness with no duplicates allowed.
   Use a List when you need to maintain insertion order, allow duplicates, or access elements by position. Use a Set when you need to ensure element uniqueness or frequently check for element existence.


5. In [GamesLoader.java](src/main/java/student/GamesLoader.java), we use a Map to help figure out the columns. What is a map? Why would we use a Map here? 

   A Map is a collection that stores key-value pairs where each key maps to exactly one value. In GamesLoader.java, a Map is used to associate column names with their positions in the CSV file. This approach allows flexibility with CSV column ordering, provides easy lookup when processing rows, and handles missing or unexpected columns nicely.


6. [GameData.java](src/main/java/student/GameData.java) is actually an `enum` with special properties we added to help with column name mappings. What is an `enum` in Java? Why would we use it for this application?

   An enum in Java is a special data type representing a fixed set of constants. GameData.java uses an enum with additional properties to map between CSV column names and application field names. This improves code readability and centralizes column name management, making the code more maintainable.






7. Rewrite the following as an if else statement inside the empty code block.
    ```java
    switch (ct) {
                case CMD_QUESTION: // same as help
                case CMD_HELP:
                    processHelp();
                    break;
                case INVALID:
                default:
                    CONSOLE.printf("%s%n", ConsoleText.INVALID);
            }
    ``` 

    ```java
    // your code here, don't forget the class name that is dropped in the switch block..
    if (ct == ConsoleText.CMD_QUESTION || ct == ConsoleText.CMD_HELP) {
        processHelp();
   } else {
        System.out.printf("%s%n", ConsoleText.INVALID);
   }
    ```

## Deeper Thinking

ConsoleApp.java uses a .properties file that contains all the strings
that are displayed to the client. This is a common pattern in software development
as it can help localize the application for different languages. You can see this
talked about here on [Java Localization – Formatting Messages](https://www.baeldung.com/java-localization-messages-formatting).

Take time to look through the console.properties file, and change some of the messages to
another language (probably the welcome message is easier). It could even be a made up language and for this - and only this - alright to use a translator. See how the main program changes, but there are still limitations in 
the current layout. 

Post a copy of the run with the updated languages below this. Use three back ticks (```) to create a code block. 

```text
// your consoles output here
```

Now, thinking about localization - we have the question of why does it matter? The obvious
one is more about market share, but there may be other reasons.  I encourage
you to take time researching localization and the importance of having programs
flexible enough to be localized to different languages and cultures. Maybe pull up data on the
various spoken languages around the world? What about areas with internet access - do they match? Just some ideas to get you started. Another question you are welcome to talk about - what are the dangers of trying to localize your program and doing it wrong? Can you find any examples of that? Business marketing classes love to point out an example of a car name in Mexico that meant something very different in Spanish than it did in English - however [Snopes has shown that is a false tale](https://www.snopes.com/fact-check/chevrolet-nova-name-spanish/).  As a developer, what are some things you can do to reduce 'hick ups' when expanding your program to other languages?


As a reminder, deeper thinking questions are meant to require some research and to be answered in a paragraph for with references. The goal is to open up some of the discussion topics in CS, so you are better informed going into industry. 