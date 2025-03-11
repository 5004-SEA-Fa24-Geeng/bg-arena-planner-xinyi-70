package student;

import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.ArrayList;
import java.util.List;


public class Planner implements IPlanner {
    /** The complete set of games available. */
    private final Set<BoardGame> allGames;

    /** The current list of games. */
    private List<BoardGame> gamesList;

    /**
     * Constructor for the Planner.
     *
     * @param games The set of all board games to be filtered and sorted
     */
    public Planner(Set<BoardGame> games) {
        this.allGames = games;
        reset();
    }

    /**
     * Filters the board games by the given filter string and sorts them by name in ascending order.
     *
     * @param filter The filter to apply to the board games.
     * @return A stream of board games that match the filter, sorted by name in ascending order.
     */
    @Override
    public Stream<BoardGame> filter(String filter) {
        return filter(filter, GameData.NAME, true);
    }

    /**
     * Filters the board games by the given filter string and sorts them by the specified column in ascending order.
     *
     * @param filter The filter to apply to the board games.
     * @param sortOn The column to sort the results on.
     * @return A stream of board games that match the filter, sorted by the specified column in ascending order.
     */
    @Override
    public Stream<BoardGame> filter(String filter, GameData sortOn) {
        return filter(filter, sortOn, true);
    }

    /**
     * Filters and sorts the list of board games based on the given criteria.
     *
     * @param filter The filter string containing conditions separated by commas.
     *               If null or empty, no filtering is applied.
     * @param sortOn The column to sort the results on.
     * @param ascending If true, sorts in ascending order; otherwise, sorts in descending order.
     * @return A stream of board games that match the filter criteria and are sorted accordingly.
     */
    @Override
    public Stream<BoardGame> filter(String filter, GameData sortOn, boolean ascending) {
        if (gamesList == null || gamesList.isEmpty()) {
            reset();
        }

        List<BoardGame> filteredGames = new ArrayList<>(gamesList);

        if (filter != null && !filter.isEmpty()) {
            String trimmedFilter = filter.trim();
            String[] filterConditions = trimmedFilter.split(",");

            for (String condition : filterConditions) {
                if (!condition.trim().isEmpty()) {
                    filteredGames = applyFilterCondition(condition.trim(), filteredGames);
                    if (filteredGames.isEmpty()) {
                        return Stream.empty();
                    }
                }
            }
        }

        return sortGames(filteredGames, sortOn, ascending).stream();
    }

    /**
     * Applies a single filter condition to the list of games.
     *
     * @param filterCondition The filter condition string (e.g. "minPlayers>=2")
     * @param games The list of games to filter
     * @return The filtered list of games
     */
    private List<BoardGame> applyFilterCondition(String filterCondition, List<BoardGame> games) {
        if (filterCondition.toLowerCase().contains("name") && filterCondition.contains("~=")) {
        // Split using ~= to properly handle the contains operator
            String[] parts = filterCondition.split("~=", 2);
            if (parts.length == 2) {
                String columnName = parts[0].trim();
                String filterValue = parts[1].trim();

                try {
                    GameData filterOn = GameData.fromString(columnName);
                    if (filterOn == GameData.NAME) {
                        List<BoardGame> result = new ArrayList<>();
                        for (BoardGame game : games) {
                            if (game.getName().toLowerCase().contains(filterValue.toLowerCase())) {
                                result.add(game);
                            }
                        }
                        return result;
                    }
                } catch (IllegalArgumentException e) {
                    return games;
                }
            }
        }

        Operations operator = Operations.getOperatorFromStr(filterCondition);
        if (operator == null) {
            return games;
        }

        String[] parts;
        if (operator == Operations.CONTAINS) {
            parts = filterCondition.split("~=", 2);
        } else {
            parts = filterCondition.split(Pattern.quote(operator.getOperator()));
        }

        if (parts.length != 2) {
            return games;
        }

        GameData filterOn;
        try {
            filterOn = GameData.fromString(parts[0].trim());
        } catch (IllegalArgumentException e) {
            return games;
        }

        String value = parts[1].trim();

        List<BoardGame> result = new ArrayList<>();
        for (BoardGame game : games) {
            if (matchesFilter(game, filterOn, operator, value)) {
                result.add(game);
            }
        }

        return result;
    }

    /**
     * Checks if a game matches a filter condition.
     *
     * @param game The game to check
     * @param filterOn The column to filter on
     * @param operator The operation to apply
     * @param value The value to compare against
     * @return True if the game matches the filter
     */
    private boolean matchesFilter(BoardGame game, GameData filterOn, Operations operator, String value) {
        switch (filterOn) {
            case NAME:
                return matchesStringFilter(game.getName(), operator, value);
            case MIN_PLAYERS:
                return matchesNumberFilter(game.getMinPlayers(), operator, value);
            case MAX_PLAYERS:
                return matchesNumberFilter(game.getMaxPlayers(), operator, value);
            case MIN_TIME:
                return matchesNumberFilter(game.getMinPlayTime(), operator, value);
            case MAX_TIME:
                return matchesNumberFilter(game.getMaxPlayTime(), operator, value);
            case DIFFICULTY:
                return matchesDoubleFilter(game.getDifficulty(), operator, value);
            case RANK:
                return matchesNumberFilter(game.getRank(), operator, value);
            case RATING:
                return matchesDoubleFilter(game.getRating(), operator, value);
            case YEAR:
                return matchesNumberFilter(game.getYearPublished(), operator, value);
            default:
                return true;
        }
    }

    /**
     * Checks if a string value matches a filter condition.
     *
     * @param gameValue The string value to check
     * @param operator The operation to apply
     * @param filterValue The value to compare against
     * @return True if the value matches the filter
     */
    private boolean matchesStringFilter(String gameValue, Operations operator, String filterValue) {
        if (gameValue == null) {
            return operator == Operations.NOT_EQUALS && filterValue != null;
        }

        switch (operator) {
            case EQUALS:
                return gameValue.equalsIgnoreCase(filterValue);
            case NOT_EQUALS:
                return !gameValue.equalsIgnoreCase(filterValue);
            case CONTAINS:
                return gameValue.toLowerCase().contains(filterValue.toLowerCase());
            case GREATER_THAN:
                return gameValue.compareToIgnoreCase(filterValue) > 0;
            case LESS_THAN:
                return gameValue.compareToIgnoreCase(filterValue) < 0;
            case GREATER_THAN_EQUALS:
                return gameValue.compareToIgnoreCase(filterValue) >= 0;
            case LESS_THAN_EQUALS:
                return gameValue.compareToIgnoreCase(filterValue) <= 0;
            default:
                return false;
        }
    }

    /**
     * Checks if an integer value matches a filter condition.
     *
     * @param gameValue The integer value to check
     * @param operator The operation to apply
     * @param filterValue The value to compare against
     * @return True if the value matches the filter
     */
    private boolean matchesNumberFilter(int gameValue, Operations operator, String filterValue) {
        try {
            int intValue = Integer.parseInt(filterValue);
            switch (operator) {
                case EQUALS:
                    return gameValue == intValue;
                case NOT_EQUALS:
                    return gameValue != intValue;
                case GREATER_THAN:
                    return gameValue > intValue;
                case LESS_THAN:
                    return gameValue < intValue;
                case GREATER_THAN_EQUALS:
                    return gameValue >= intValue;
                case LESS_THAN_EQUALS:
                    return gameValue <= intValue;
                default:
                    return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if a double value matches a filter condition.
     *
     * @param gameValue The double value to check
     * @param operator The operation to apply
     * @param filterValue The value to compare against
     * @return True if the value matches the filter
     */
    private boolean matchesDoubleFilter(double gameValue, Operations operator, String filterValue) {
        try {
            double doubleValue = Double.parseDouble(filterValue);
            switch (operator) {
                case EQUALS:
                    return Math.abs(gameValue - doubleValue) < 0.001;
                case NOT_EQUALS:
                    return Math.abs(gameValue - doubleValue) >= 0.001;
                case GREATER_THAN:
                    return gameValue > doubleValue;
                case LESS_THAN:
                    return gameValue < doubleValue;
                case GREATER_THAN_EQUALS:
                    return gameValue >= doubleValue;
                case LESS_THAN_EQUALS:
                    return gameValue <= doubleValue;
                default:
                    return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Sorts a list of games by a specific column.
     *
     * @param games The list of games to sort
     * @param sortOn The column to sort on
     * @param ascending Whether to sort in ascending order
     * @return The sorted list of games
     */
    private List<BoardGame> sortGames(List<BoardGame> games, GameData sortOn, boolean ascending) {
        List<BoardGame> sortedList = new ArrayList<>(games);
        sortedList.sort(GameSorter.sortFilteredGames(sortOn, ascending));
        return sortedList;
    }

    @Override
    public void reset() {
        gamesList = new ArrayList<>(allGames);
    }
}
