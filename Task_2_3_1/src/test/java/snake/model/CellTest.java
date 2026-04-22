package snake.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CellTest {
    @Test
    public void testTranslate() {
        Cell cell = new Cell(5, 5);
        Cell translated = cell.translate(Direction.UP);
        assertEquals(5, translated.x());
        assertEquals(4, translated.y());
    }

    @Test
    public void testEqualsAndHashCode() {
        Cell cell1 = new Cell(2, 3);
        Cell cell2 = new Cell(2, 3);
        Cell cell3 = new Cell(3, 2);

        assertEquals(cell1, cell2);
        assertNotEquals(cell1, cell3);
        assertNotEquals(cell1, null);
        assertEquals(cell1.hashCode(), cell2.hashCode());
    }
}

