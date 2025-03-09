package student;

import java.util.stream.Stream;

/**
 * Sets up filters for the board game data.
 * <p>
 * This the primary interface for the program. DO NOT MODIFY THIS FILE.
 * <p>
 * Students, you will need to implement the methods in this interface in a class called
 * Planner.java. You can assume the constructor of Planner.java takes in a Set<BoardGame> as a
 * parameter. This represents the total board game collection.
 * <p>
 * An important note, while most of methods return streams, each method builds on each other / is
 * progressive. As such if filter by minPlayers, then filter by maxPlayers, the maxPlayers filter
 * should be applied to the results of the minPlayers filter unless reset is called between.
 */
public interface IPlanner {

    /**
     * Assumes the results are sorted in ascending order, and that the steam is sorted by the name
     * of the board game (GameData.NAME).
     *
     * @param filter The filter to apply to the board games.
     * @return A stream of board games that match the filter.
     * @see #filter(String, GameData, boolean)
     */
    Stream<BoardGame> filter(String filter);

    /**
     * Filters the board games by the passed in text filter. Assumes the results are sorted in
     * ascending order.
     *
     * @param filter The filter to apply to the board games.
     * @param sortOn The column to sort the results on.
     * @return A stream of board games that match the filter.
     * @see #filter(String, GameData, boolean)
     */
    Stream<BoardGame> filter(String filter, GameData sortOn);

    /**
     * Filters the board games by the passed in text filter.
     * <p>
     * <p>
     * A text filter can contain the following options:
     * <p>
     * > : greater than
     * <p>
     * < : less than
     * <p>
     * >= : greater than or equal to
     * <p>
     * <= : less than or equal to
     * <p>
     * == : equal to
     * <p>
     * != : not equal to
     * <p>
     * ~= : contains the text
     * <p>
     * The left side of the filter describes the column to filter on. The right side of the filter
     * describes the value to filter on.
     * <p>
     * Fo example:
     * <p>
     * minPlayers>4
     * <p>
     * would filter the board games to only those with a minimum number of players greater than 4.
     * <p>
     * Commas between filters are treated as ANDs. For example:
     * <p>
     * minPlayers>4,maxPlayers<6
     * <p>
     * It is possible to filter on the same column multiple times. For example:
     * <p>
     * minPlayers>4,minPlayers<6
     * <p>
     * This would filter the board games to only those with a minimum number of players greater than
     * 4 and less than 6.
     * <p>
     * Spaces should be ignored, but can be included for readability. For example:
     * <p>
     * minPlayers > 4
     * <p>
     * is the same as
     * <p>
     * minPlayers>4
     * <p>
     * <p>
     * If filtering on a string column, the filter should be case insensitive. For example:
     * <p>
     * name~=pandemic
     * <p>
     * would filter the board games to only those with the word "pandemic" in the name, but could
     * also have Pandemic or PANDEMIC.
     * <p>
     * Column names will match the values in GameData. As such is it possible to use
     * <p>
     * GameData.MIN_PLAYERS.getColumnName() or GameData.fromString("minplayers") to get the column
     * name for the minPlayers column.
     * <p>
     * Note: id is a special column that is not used for filtering or sorting.
     * <p>
     * if the filter is empty (""), then the results should return the current filter sorted based
     * on the sortOn column and in the defined direction.
     *
     * @param filter    The filter to apply to the board games.
     * @param sortOn    The column to sort the results on.
     * @param ascending Whether to sort the results in ascending order or descending order.
     * @return A stream of board games that match the filter.
     */
    Stream<BoardGame> filter(String filter, GameData sortOn, boolean ascending);

    /**
     * Resets the collection to have no filters applied.
     */
    void reset();

}
