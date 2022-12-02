package edu.uoc.trip.model.cells;

import edu.uoc.trip.model.utils.Coordinate;
import org.junit.jupiter.api.*;

import java.lang.reflect.Modifier;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
class MovableCellTest {

    private MovableCell c;
    private final Class<MovableCell> ownClass = MovableCell.class;

    @BeforeAll
    void setUp() {
        c = new MovableCell(31, 4, CellType.BOTTOM_LEFT);
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity - Class definition")
    void checkClassSanity() {
        //Class declaration
        int modifiers = ownClass.getModifiers();
        assertTrue(Modifier.isPublic(modifiers));
        assertFalse(Modifier.isStatic(modifiers));
        assertFalse(Modifier.isFinal(modifiers));

        assertInstanceOf(Cell.class, c);
        assertInstanceOf(Movable.class, c);
        assertFalse(c instanceof Rotatable);

        assertEquals("edu.uoc.trip.model.cells",ownClass.getPackageName());
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity - Fields definition")
    void checkFieldsSanity() {
        //All fields must be private
        assertTrue(Arrays.stream(ownClass.getDeclaredFields()).allMatch(p -> Modifier.isPrivate(p.getModifiers())));
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity - Methods and Constructors definition")
    void checkMethodsSanity() {
        //Max 3 public methods
        assertEquals(3, Arrays.stream(ownClass.getDeclaredMethods()).filter(p -> Modifier.isPublic(p.getModifiers())).count());
        //Max 0 protected methods
        assertEquals(0, Arrays.stream(ownClass.getDeclaredMethods()).filter(p -> Modifier.isProtected(p.getModifiers())).count());
        //Max 0 package-private methods
        assertEquals(0, Arrays.stream(ownClass.getDeclaredMethods()).filter(p -> Modifier.isNative(p.getModifiers())).count());

        //Number of constructors
        assertEquals(1, ownClass.getDeclaredConstructors().length);

        //Constructor
        try {
            int modifiers = ownClass.getDeclaredConstructor(int.class, int.class, CellType.class).getModifiers();
            assertTrue(Modifier.isPublic(modifiers));
        } catch (NoSuchMethodException e) {
            fail("The definition of MovableCell class is wrong. Please read the PRAC 2 - Statement:\n");
            e.printStackTrace();
        }
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - isMovable")
    void isMovable() {
        assertTrue(c.isMovable());
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - isRotatable")
    void isRotatable() {
        assertFalse(c.isRotatable());
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - move")
    void move() {
        Coordinate coord = new Coordinate(8, 9);
        c.move(coord);
        assertEquals(coord, c.getCoordinate());
        assertEquals(8, c.getCoordinate().getRow());
        assertEquals(9, c.getCoordinate().getColumn());

        coord = new Coordinate(31, 4);
        c.move(coord);
        assertEquals(coord, c.getCoordinate());
        assertEquals(31, c.getCoordinate().getRow());
        assertEquals(4, c.getCoordinate().getColumn());
    }
}
