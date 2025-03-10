import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import student.BoardGame;
import static org.junit.jupiter.api.Assertions.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import student.Planner;
import student.IPlanner;
import student.GameData;


/**
 * JUnit test for the Planner class.
 * <p>
 * Just a sample test to get you started, also using
 * setup to help out. 
 */
public class TestPlanner {
    static Set<BoardGame> games;

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
    }

     @Test
    public void testFilterName() {
        IPlanner planner = new Planner(games);
        List<BoardGame> filtered = planner.filter("name == Go").toList();
        assertEquals(1, filtered.size());
        assertEquals("Go", filtered.get(0).getName());
    }

    @Test
    public void testFilterContainsName() {
        IPlanner planner = new Planner(games);
        List<BoardGame> filtered = planner.filter("name ~= Go").toList();
        assertEquals(4, filtered.size()); // Go, Go Fish, golang, GoRami
    }

    @Test
    public void testFilterByNameEquals() {
        IPlanner planner = new Planner(games);
        List<BoardGame> filtered = planner.filter("name == Go").toList();
        assertEquals(1, filtered.size());
    }

    @Test
    public void testNameGreaterThan() {
        IPlanner planner = new Planner(games);
        List<BoardGame> filtered = planner.filter("name > Go").toList();
        assertEquals(5, filtered.size()); // Go Fish, golang, GoRami, Monopoly, Tucano
    }

    @Test
    public void testNameLessThan() {
        IPlanner planner = new Planner(games);
        List<BoardGame> filtered = planner.filter("name < Go").toList();
        assertEquals(2, filtered.size()); // 17 days, Chess
    }

    @Test
    public void testNameGreaterThanOrEqual() {
        IPlanner planner = new Planner(games);
        List<BoardGame> filtered = planner.filter("name >= Go").toList();
        assertEquals(6, filtered.size()); // Go, Go Fish, golang, GoRami, Monopoly, Tucano
    }

    @Test
    public void testNameLessThanOrEqual() {
        IPlanner planner = new Planner(games);
        List<BoardGame> filtered = planner.filter("name <= Go").toList();
        assertEquals(3, filtered.size()); // 17 days, Chess, Go
    }

    @Test
    public void testFilterMinPlayers() {
        IPlanner planner = new Planner(games);
        List<BoardGame> filtered = planner.filter("minPlayers > 5").toList();
        assertEquals(3, filtered.size()); // GoRami, Monopoly, Tucano
    }

    @Test
    public void testFilterMaxPlayers() {
        IPlanner planner = new Planner(games);
        List<BoardGame> filtered = planner.filter("maxPlayers >= 10").toList();
        assertEquals(3, filtered.size()); // Go Fish, Monopoly, Tucano
    }

    @Test
    public void testFilterMaxPlayTime() {
        IPlanner planner = new Planner(games);
        List<BoardGame> filtered = planner.filter("maxPlaytime > 100").toList();
        assertEquals(2, filtered.size()); // Go Fish, Monopoly
    }

    @Test
    public void testFilterMinPlayTime() {
        IPlanner planner = new Planner(games);
        List<BoardGame> filtered = planner.filter("minPlaytime < 20").toList();
        assertEquals(1, filtered.size()); // Chess
    }

    @Test
    public void testFilterDifficulty() {
        IPlanner planner = new Planner(games);
        List<BoardGame> filtered = planner.filter("difficulty <= 5.0").toList();
        assertEquals(3, filtered.size()); // Go Fish, GoRami, Monopoly
    }

    @Test
    public void testFilterRank() {
        IPlanner planner = new Planner(games);
        List<BoardGame> filtered = planner.filter("rank < 300").toList();
        assertEquals(2, filtered.size()); // Go, Go Fish
    }

    @Test
    public void testFilterRating() {
        IPlanner planner = new Planner(games);
        List<BoardGame> filtered = planner.filter("rating >= 9.0").toList();
        assertEquals(3, filtered.size()); // 17 days, Chess, golang
    }

    @Test
    public void testFilterYear() {
        IPlanner planner = new Planner(games);
        List<BoardGame> filtered = planner.filter("year < 2002").toList();
        assertEquals(2, filtered.size()); // Go, Go Fish
    }

    @Test
    public void testFilterMultiple() {
        IPlanner planner = new Planner(games);
        List<BoardGame> filtered = planner.filter("minPlayers >= 2, maxPlayers <= 5").toList();
        assertEquals(2, filtered.size()); // Chess, Go
    }

    @Test
    public void testEmptyFilter() {
        IPlanner planner = new Planner(games);
        List<BoardGame> filtered = planner.filter("").toList();
        assertEquals(games.size(), filtered.size());
    }

    @Test
    public void testFilterWithSortAscending() {
        IPlanner planner = new Planner(games);
        List<BoardGame> filtered = planner.filter("minPlayers >= 2", GameData.YEAR).toList();
        assertEquals(7, filtered.size());
        assertEquals(2000, filtered.get(0).getYearPublished());
        assertEquals(2007, filtered.get(6).getYearPublished());
    }

    @Test
    public void testFilterWithSortDescending() {
        IPlanner planner = new Planner(games);
        List<BoardGame> filtered = planner.filter("", GameData.RATING, false).toList();
        assertEquals(8, filtered.size());
        assertEquals(10.0, filtered.get(0).getRating());
        assertEquals(5.0, filtered.get(7).getRating());
    }

    @Test
    public void testReset() {
        IPlanner planner = new Planner(games);
        planner.filter("minPlayers > 5");
        planner.reset();
        List<BoardGame> afterReset = planner.filter("").toList();
        assertEquals(games.size(), afterReset.size());
    }

    @Test
    public void testInvalidFilter() {
        IPlanner planner = new Planner(games);
        List<BoardGame> filtered = planner.filter("invalid filter").toList();
        assertEquals(games.size(), filtered.size());
    }
}