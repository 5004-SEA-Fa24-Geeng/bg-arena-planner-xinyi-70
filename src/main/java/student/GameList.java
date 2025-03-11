package student;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.ArrayList;


public class GameList implements IGameList {
    /** The set of games in the list. */
    private final Set<BoardGame> listOfGames;

    /**
     * Constructor for the GameList.
     */
    public GameList() {
        this.listOfGames = new HashSet<>();
    }

    /**
     * Gets the contents of the list as a list of names (Strings) in ascending order.
     *
     * @return the list of game names in ascending order ignoring case.
     */
    @Override
    public List<String> getGameNames() {
        return listOfGames.stream()
                .map(BoardGame::getName)
                .sorted((s1, s2) -> s1.toLowerCase().compareTo(s2.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Removes all games in the list.
     */
    @Override
    public void clear() {
        listOfGames.clear();
    }

    /**
     * Returns the number of games in the list.
     *
     * @return the number of games in the list.
     */
    @Override
    public int count() {
        return listOfGames.size();
    }

    /**
     * Saves the list of games to a file.
     * <p>
     * The contents of the file will be each game name on a new line. It will overwrite the file if it already exists.
     * <p>
     * Saves them in the same order as getGameNames().
     *
     * @param filename The name of the file to save the list to.
     */
    @Override
    public void saveGame(String filename) {
        List<String> gameNames = getGameNames();
        try {
            java.nio.file.Files.write(java.nio.file.Path.of(filename), gameNames);
        } catch (java.io.IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    /**
     * Adds a game or games to the list based on the given input string.
     * <p>
     * If the input string is invalid, an {@code IllegalArgumentException} will be thrown.
     *
     * @param str the string to parse and add games to the list.
     * @param filtered the filtered stream of games to use as a reference for adding.
     * @throws IllegalArgumentException if the string is empty, out of range, or invalid.
     */
    @Override
    public void addToList(String str, Stream<BoardGame> filtered) throws IllegalArgumentException {
        if (str == null || str.trim().isEmpty()) {
            throw new IllegalArgumentException("Input string cannot be empty");
        }

        str = str.trim().toLowerCase();

        // Convert filtered stream to list for multiple operations
        List<BoardGame> filteredList = filtered.collect(Collectors.toList());

        // If no games found
        if (filteredList.isEmpty()) {
            throw new IllegalArgumentException("No games in filtered list");
        }

        // Check if we need to add all games
        if (str.equals(IGameList.ADD_ALL)) {
            listOfGames.addAll(filteredList);
            return;
        }

        // Check if we're adding a range
        if (str.contains("-")) {
            addRange(str, filteredList);
            return;
        }

        // Try to parse as a number
        try {
            int index = Integer.parseInt(str);
            if (index < 1 || index > filteredList.size()) {
                throw new IllegalArgumentException("Index out of range: " + index);
            }
            listOfGames.add(filteredList.get(index - 1));
            return;
        } catch (NumberFormatException e) {
            // Not a number, try to find by name
        }

        // Try to find game by name using stream
        BoardGame matchingGame = null;
        for (BoardGame game : filteredList) {
            if (game.getName().equalsIgnoreCase(str)) {
                matchingGame = game;
                break;
            }
        }

        if (matchingGame == null) {
            throw new IllegalArgumentException("Game not found: " + str);
        }

        listOfGames.add(matchingGame);
    }

    /**
     * Add a range of games to the list.
     *
     * @param range The range string (e.g., "1-5")
     * @param filteredList The list of filtered games
     * @throws IllegalArgumentException If the range is invalid
     */
    private void addRange(String range, List<BoardGame> filteredList) throws IllegalArgumentException {
        String[] parts = range.split("-");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid range format: " + range);
        }

        try {
            int start = Integer.parseInt(parts[0]);
            int end = Integer.parseInt(parts[1]);

            if (start < 1 || start > filteredList.size() || end < 1 || end > filteredList.size()) {
                throw new IllegalArgumentException("Range out of bounds: " + range);
            }

            if (start > end) {
                throw new IllegalArgumentException("Invalid range (start > end): " + range);
            }

            for (int i = start; i <= end; i++) {
                listOfGames.add(filteredList.get(i - 1));
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid range numbers: " + range);
        }
    }

    /**
     * Removes a game or games from the list based on the given input string.
     * <p>
     * If the input string is invalid or out of range, an {@code IllegalArgumentException} will be thrown.
     *
     * @param str the string to parse and remove games from the list.
     * @throws IllegalArgumentException if the string is empty, out of range, or invalid.
     */
    @Override
    public void removeFromList(String str) throws IllegalArgumentException {
        if (str == null || str.trim().isEmpty()) {
            throw new IllegalArgumentException("Input string cannot be empty");
        }

        str = str.trim().toLowerCase();

        // Check if we need to remove all games
        if (str.equals(IGameList.ADD_ALL)) {
            clear();
            return;
        }

        // Get the current list of games
        List<BoardGame> gamesList = new ArrayList<>(listOfGames);
        gamesList.sort((g1, g2) -> g1.getName().toLowerCase().compareTo(g2.getName().toLowerCase()));

        // Check if we're removing a range
        if (str.contains("-")) {
            removeRange(str, gamesList);
            return;
        }

        // Try to parse as a number
        try {
            int index = Integer.parseInt(str);
            if (index < 1 || index > gamesList.size()) {
                throw new IllegalArgumentException("Index out of range: " + index);
            }
            listOfGames.remove(gamesList.get(index - 1));
            return;
        } catch (NumberFormatException e) {
            // Not a number, try to find by name
        }

        // Try to find game by name using stream
        BoardGame matchingGame = null;
        for (BoardGame game : listOfGames) {
            if (game.getName().equalsIgnoreCase(str)) { // 直接使用 str
                matchingGame = game;
                break;
            }
        }

        if (matchingGame == null) {
            throw new IllegalArgumentException("Game not found: " + str); // 使用 str
        }

        listOfGames.remove(matchingGame);
    }

    /**
     * Remove a range of games from the list.
     *
     * @param range The range string (e.g., "1-5")
     * @param gamesList The sorted list of games in the current list
     * @throws IllegalArgumentException If the range is invalid
     */
    private void removeRange(String range, List<BoardGame> gamesList) throws IllegalArgumentException {
        String[] parts = range.split("-");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid range format: " + range);
        }

        try {
            int start = Integer.parseInt(parts[0]);
            int end = Integer.parseInt(parts[1]);

            if (start < 1 || start > gamesList.size() || end < 1 || end > gamesList.size()) {
                throw new IllegalArgumentException("Range out of bounds: " + range);
            }

            if (start > end) {
                throw new IllegalArgumentException("Invalid range (start > end): " + range);
            }

            List<BoardGame> toRemove = new ArrayList<>();
            for (int i = start - 1; i < end; i++) {
                toRemove.add(gamesList.get(i));
            }
            
            listOfGames.removeAll(toRemove);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid range numbers: " + range);
        }
    }
}
