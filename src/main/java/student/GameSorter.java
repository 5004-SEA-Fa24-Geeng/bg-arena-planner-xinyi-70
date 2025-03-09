package student;

import java.util.Comparator;

/**
 * Provides comparators for sorting BoardGame objects.
 * Implements the strategy pattern for sorting.
 */
public class GameSorter {

    /**
     * Creates a comparator for sorting filtered games.
     *
     * @param sortOn The column to sort on
     * @param ascending Whether to sort in ascending order
     * @return A comparator for sorting games
     */
    public static Comparator<BoardGame> sortFilteredGames(GameData sortOn, boolean ascending) {
        Comparator<BoardGame> comparator = getBaseComparator(sortOn)
                .thenComparing(g -> g.getName().toLowerCase());

        return ascending ? comparator : comparator.reversed();
    }

    /**
     * Gets the base comparator for a column (always ascending).
     *
     * @param sortOn The column to sort on
     * @return A base comparator for the column
     */
    private static Comparator<BoardGame> getBaseComparator(GameData sortOn) {
        switch (sortOn) {
            case NAME:
                return Comparator.comparing(g -> g.getName().toLowerCase()); // 确保忽略大小写
            case MIN_PLAYERS:
                return Comparator.comparingInt(BoardGame::getMinPlayers);
            case MAX_PLAYERS:
                return Comparator.comparingInt(BoardGame::getMaxPlayers);
            case MIN_TIME:
                return Comparator.comparingInt(BoardGame::getMinPlayTime);
            case MAX_TIME:
                return Comparator.comparingInt(BoardGame::getMaxPlayTime);
            case DIFFICULTY:
                return Comparator.comparingDouble(BoardGame::getDifficulty);
            case RANK:
                return Comparator.comparingInt(BoardGame::getRank);
            case RATING:
                return Comparator.comparingDouble(BoardGame::getRating);
            case YEAR:
                return Comparator.comparingInt(BoardGame::getYearPublished);
            default:
                return Comparator.comparing(g -> g.getName().toLowerCase());
        }
    }
}