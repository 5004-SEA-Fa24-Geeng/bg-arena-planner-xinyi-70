import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import student.BoardGame;
import student.GameList;
import student.IGameList;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * JUnit test for the GameList class.
 */
public class GameListTest {
    private IGameList gameList;
    private static Set<BoardGame> games;

    @BeforeAll
    public static void setupAll() {
        games = new HashSet<>();
        games.add(new BoardGame("17 days", 6, 1, 8, 70, 70, 9.0, 600, 9.0, 2005));
        games.add(new BoardGame("Chess", 7, 2, 2, 10, 20, 10.0, 700, 10.0, 2006));
        games.add(new BoardGame("Go", 1, 2, 5, 30, 30, 8.0, 100, 7.5, 2000));
        games.add(new BoardGame("Go Fish", 2, 2, 10, 20, 120, 3.0, 200, 6.5, 2001));
        games.add(new BoardGame("golang", 4, 2, 7, 50, 55, 7.0, 400, 9.5, 2003));
        games.add(new BoardGame("GoRami", 3, 6, 6, 40, 42, 5.0, 300, 8.5, 2002));
        games.add(new BoardGame("Monopoly", 8, 6, 10, 20, 1000, 1.0, 800, 5.0, 2007));
        games.add(new BoardGame("Tucano", 5, 10, 20, 60, 90, 6.0, 500, 8.0, 2004));
    }

    @BeforeEach
    public void setup() {
        gameList = new GameList();
    }

    @Test
    public void testNewGameListIsEmpty() {
        assertEquals(0, gameList.count());
        assertTrue(gameList.getGameNames().isEmpty());
    }

    @Test
    public void testAddSingleGameByNumber() {
        List<BoardGame> gamesList = new ArrayList<>(games);
        gameList.addToList("1", gamesList.stream());
        assertEquals(1, gameList.count());
    }

    @Test
    public void testAddSingleGameByName() {
        gameList.addToList("Chess", games.stream());
        assertEquals(1, gameList.count());
        assertTrue(gameList.getGameNames().contains("Chess"));
    }

    @Test
    public void testAddGamesByRange() {
        gameList.addToList("1-3", games.stream());
        assertEquals(3, gameList.count());
    }

    @Test
    public void testAddAllGames() {
        gameList.addToList("all", games.stream());
        assertEquals(games.size(), gameList.count());
    }

    @Test
    public void testAddSingleRange() {
        gameList.addToList("1-1", games.stream());
        assertEquals(1, gameList.count());
    }

    @Test
    public void testAddDuplicateGames() {
        gameList.addToList("Chess", games.stream());
        gameList.addToList("Chess", games.stream());
        assertEquals(1, gameList.count());
    }

    @Test
    public void testAddInvalidRange() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> gameList.addToList("5-1", games.stream()));
        assertTrue(exception.getMessage().contains("Invalid range"));
    }

    @Test
    public void testAddNegativeIndex() {
        assertThrows(IllegalArgumentException.class,
                () -> gameList.addToList("-1", games.stream()));
    }

    @Test
    public void testRemoveSingleGameByNumber() {
        gameList.addToList("all", games.stream());
        int initialCount = gameList.count();
        gameList.removeFromList("1");
        assertEquals(initialCount - 1, gameList.count());
    }

    @Test
    public void testRemoveSingleGameByName() {
        gameList.addToList("all", games.stream());
        gameList.removeFromList("Chess");
        assertFalse(gameList.getGameNames().contains("Chess"));
    }

    @Test
    public void testRemoveNonExistentGame() {
        gameList.addToList("all", games.stream());
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> gameList.removeFromList("NonExistentGame"));
        assertTrue(exception.getMessage().contains("Game not found"));
    }

    @Test
    public void testClear() {
        gameList.addToList("all", games.stream());
        gameList.clear();
        assertEquals(0, gameList.count());
    }

    @Test
    public void testSaveGame() throws IOException {
        String testFilename = "test_games_list.txt";
        gameList.addToList("1-3", games.stream());
        gameList.saveGame(testFilename);
        Path path = Path.of(testFilename);
        List<String> lines = Files.readAllLines(path);
        assertEquals(3, lines.size());
        Files.delete(path);
    }
}
