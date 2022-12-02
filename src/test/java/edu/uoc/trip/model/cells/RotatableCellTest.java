package edu.uoc.trip.model.cells;

import org.junit.jupiter.api.*;

import java.lang.reflect.Modifier;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
class RotatableCellTest {

    private RotatableCell c;
    private final Class<RotatableCell> ownClass = RotatableCell.class;

    @BeforeAll
    void setUp(){
        c = new RotatableCell(-15,40,CellType.ROTATABLE_HORIZONTAL);
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
        assertInstanceOf(Rotatable.class, c);
        assertFalse(c instanceof Movable);

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
        assertEquals(0,Arrays.stream(ownClass.getDeclaredMethods()).filter(p -> Modifier.isProtected(p.getModifiers())).count());
        //Max 0 package-private methods
        assertEquals(0,Arrays.stream(ownClass.getDeclaredMethods()).filter(p -> Modifier.isNative(p.getModifiers())).count());

        //Number of constructors
        assertEquals(1,ownClass.getDeclaredConstructors().length);

        //Constructor
        try {
            int modifiers = ownClass.getDeclaredConstructor(int.class,int.class,CellType.class).getModifiers();
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
        assertFalse(c.isMovable());
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - isRotatable")
    void isRotatable() {
        assertTrue(c.isRotatable());
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - rotate")
    void rotate() {
        assertEquals(CellType.ROTATABLE_HORIZONTAL,c.getType());
        c.rotate();
        assertEquals(CellType.ROTATABLE_VERTICAL,c.getType());
        c.rotate();
        assertEquals(CellType.ROTATABLE_HORIZONTAL,c.getType());

        RotatableCell c2 = new RotatableCell(1,1,CellType.BOTTOM_LEFT);
        c2.rotate();
        assertEquals(CellType.TOP_LEFT,c2.getType());
        c2.rotate();
        assertEquals(CellType.TOP_RIGHT,c2.getType());
        c2.rotate();
        assertEquals(CellType.BOTTOM_RIGHT,c2.getType());
        c2.rotate();
        assertEquals(CellType.BOTTOM_LEFT,c2.getType());
    }
}
