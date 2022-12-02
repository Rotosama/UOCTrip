package edu.uoc.trip.model.cells;

import edu.uoc.trip.model.utils.Coordinate;
import org.junit.jupiter.api.*;

import java.lang.reflect.Modifier;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
class CellTest {

    private Cell c;
    private final Class<Cell> ownClass = Cell.class;

    @BeforeAll
    void setUp(){
        c = new Cell(31,4,CellType.FREE);
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
    @DisplayName("Sanity - Methods definition")
    void checkMethodsSanity() {
        //Min 7 methods
        assertTrue(ownClass.getDeclaredMethods().length>=7);
        //Max 5 public methods
        assertEquals(5, Arrays.stream(ownClass.getDeclaredMethods()).filter(p -> Modifier.isPublic(p.getModifiers())).count());
        //Max 0 protected methods
        assertEquals(2,Arrays.stream(ownClass.getDeclaredMethods()).filter(p -> Modifier.isProtected(p.getModifiers())).count());
        //Max 0 package-private methods
        assertEquals(0,Arrays.stream(ownClass.getDeclaredMethods()).filter(p -> Modifier.isNative(p.getModifiers())).count());

        try{
            //These methods must be protected
            assertTrue(Modifier.isProtected(ownClass.getDeclaredMethod("setType", CellType.class).getModifiers()));
            assertTrue(Modifier.isProtected(ownClass.getDeclaredMethod("setCoordinate", int.class,int.class).getModifiers()));

            //Number of constructors
            assertEquals(1,ownClass.getDeclaredConstructors().length);
            //Constructor
            int modifiers = ownClass.getDeclaredConstructor(int.class,int.class,CellType.class).getModifiers();
            assertTrue(Modifier.isPublic(modifiers));

        } catch (NoSuchMethodException e) {
            fail("There is some problem with the definition of Cell's methods/constructors. Please read the PRAC 2 - Statement:\n");
            e.printStackTrace();
        }
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - getType")
    void getType() {
        assertEquals(CellType.FREE, c.getType());
        assertNotEquals(CellType.START, c.getType());
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - setType")
    void setType() {
        c.setType(CellType.RIVER);
        assertEquals(CellType.RIVER,c.getType());

        c.setType(CellType.FREE);
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - getCoordinate")
    void getCoordinate() {
        assertEquals(31, c.getCoordinate().getRow());
        assertEquals(4, c.getCoordinate().getColumn());
        assertEquals(new Coordinate(31,4), c.getCoordinate());
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - setCoordinate")
    void setCoordinate() {
        c.setCoordinate(5,-2);
        assertEquals(5, c.getCoordinate().getRow());
        assertEquals(-2, c.getCoordinate().getColumn());
        c.setCoordinate(31,4);
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - isMovable")
    void isMovable() {
        assertFalse(c.isMovable());
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - isRotatable")
    void isRotatable() {
        assertFalse(c.isMovable());
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - toString")
    void testToString() {
        assertEquals(String.valueOf('\u00b7'),c.toString());
    }
}
