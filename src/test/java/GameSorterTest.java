import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import student.BoardGame;
import student.GameData;
import student.GameSorter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit test for the GameSorter class.
 */
public class GameSorterTest {
    private static Set<BoardGame> games;
    private static List<BoardGame> gamesList;

    @BeforeAll
    public static void setup() {
        games = new HashSet<>();
        games.add(new BoardGame("17 days", 6, 1, 8, 70, 70, 9.0, 600, 9.0, 2005));
        games.add(new BoardGame("Chess", 7, 2, 2, 10, 20, 10.0, 700, 10.0, 2006));
        games.add(new BoardGame("Go", 1, 2, 5, 30, 30, 8.0, 100, 7.5, 2000));
        games.add(new BoardGame("Go Fish", 2, 2, 10, 20, 120, 3.0, 200, 6.5, 2001));
        games.add(new BoardGame("golang", 4, 2, 7, 50, 55, 7.0, 400, 9.5, 2003));
        games.add(new BoardGame("GoRami", 3, 6, 6, 40, 42, 5.0, 300, 8.5, 2002));
        games.add(new BoardGame("Monopoly", 8, 6, 10, 20, 1000, 1.0, 800, 5.0, 2007));
        games.add(new BoardGame("Tucano", 5, 10, 20, 60, 90, 6.0, 500, 8.0, 2004));

        gamesList = new ArrayList<>(games);
    }

    @Test
    public void testSortByName() {
        List<BoardGame> sorted = new ArrayList<>(gamesList);
        sorted.sort(GameSorter.sortFilteredGames(GameData.NAME, true));

        assertEquals("17 days", sorted.get(0).getName());
        assertEquals("Tucano", sorted.get(sorted.size() - 1).getName());

        for (int i = 0; i < sorted.size() - 1; i++) {
            assertTrue(sorted.get(i).getName().compareToIgnoreCase(sorted.get(i + 1).getName()) <= 0);
        }
    }

    @Test
    public void testSortByNameDescending() {
        List<BoardGame> sorted = new ArrayList<>(gamesList);
        sorted.sort(GameSorter.sortFilteredGames(GameData.NAME, false));

        assertEquals("Tucano", sorted.get(0).getName());
        assertEquals("17 days", sorted.get(sorted.size() - 1).getName());
    }

    @Test
    public void testSortByMinPlayers() {
        List<BoardGame> sorted = new ArrayList<>(gamesList);
        sorted.sort(GameSorter.sortFilteredGames(GameData.MIN_PLAYERS, true));

        assertEquals(1, sorted.get(0).getMinPlayers()); // 17 days
        assertEquals(10, sorted.get(sorted.size() - 1).getMinPlayers()); // Tucano
    }

    @Test
    public void testSortByDifficulty() {
        List<BoardGame> sorted = new ArrayList<>(gamesList);
        sorted.sort(GameSorter.sortFilteredGames(GameData.DIFFICULTY, true));

        assertEquals(1.0, sorted.get(0).getDifficulty()); // Monopoly
        assertEquals(10.0, sorted.get(sorted.size() - 1).getDifficulty()); // Chess
    }

    @Test
    public void testSortByRating() {
        List<BoardGame> sorted = new ArrayList<>(gamesList);
        sorted.sort(GameSorter.sortFilteredGames(GameData.RATING, true));

        assertEquals(5.0, sorted.get(0).getRating()); // Monopoly
        assertEquals(10.0, sorted.get(sorted.size() - 1).getRating()); // Chess
    }

    @Test
    public void testSortByYearDescending() {
        List<BoardGame> sorted = new ArrayList<>(gamesList);
        sorted.sort(GameSorter.sortFilteredGames(GameData.YEAR, false));

        assertEquals(2007, sorted.get(0).getYearPublished()); // Monopoly
        assertEquals(2000, sorted.get(sorted.size() - 1).getYearPublished()); // Go
    }

    @Test
    public void testSortTiebreaker() {
        BoardGame game1 = new BoardGame("AAA Game", 10, 2, 4, 30, 60, 2.0, 900, 7.0, 2010);
        BoardGame game2 = new BoardGame("ZZZ Game", 11, 2, 4, 30, 60, 2.0, 901, 7.0, 2010);

        List<BoardGame> testGames = new ArrayList<>();
        testGames.add(game2);
        testGames.add(game1);

        testGames.sort(GameSorter.sortFilteredGames(GameData.MIN_PLAYERS, true));

        assertEquals("AAA Game", testGames.get(0).getName());
        assertEquals("ZZZ Game", testGames.get(1).getName());
    }

    @Test
    public void testSortDefault() {
        List<BoardGame> sorted = new ArrayList<>(gamesList);
        sorted.sort(GameSorter.sortFilteredGames(GameData.ID, true));

        assertEquals("17 days", sorted.get(0).getName());
        assertEquals("Tucano", sorted.get(sorted.size() - 1).getName());
    }
}