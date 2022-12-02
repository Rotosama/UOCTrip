package edu.uoc.trip.model.cells;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Arrays;

import static java.lang.reflect.Modifier.isStatic;
import static java.lang.reflect.Modifier.isPublic;
import static java.lang.reflect.Modifier.isPrivate;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
class CellFactoryTest {

    private final Class<CellFactory> ownClass = CellFactory.class;

    @Test
    @Tag("sanity")
    @DisplayName("Sanity - Fields definition")
    void checkFieldsSanity() {
        //All fields must be private
        assertTrue(Arrays.stream(ownClass.getDeclaredFields()).allMatch(p -> isPrivate(p.getModifiers())));
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity - Methods definition")
    void checkMethodsSanity() {
        assertEquals(1, ownClass.getDeclaredMethods().length);
        try {
            assertTrue(isPublic(ownClass.getDeclaredMethod("getCellInstance", int.class, int.class, CellType.class).getModifiers()));
            assertTrue(isStatic(ownClass.getDeclaredMethod("getCellInstance", int.class, int.class, CellType.class).getModifiers()));
        } catch (NoSuchMethodException e) {
            fail("There is some problem with the definition of Cell's methods/constructors. Please read the PRAC 2 - Statement:\n");
            e.printStackTrace();
        }
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity - getCellInstance definition")
    void getCellInstance() {
        assertTrue(CellFactory.getCellInstance(0, 0, CellType.TOP_LEFT) instanceof MovableCell);
        assertTrue(CellFactory.getCellInstance(0, 0, CellType.TOP_LEFT) instanceof MovableCell);
        assertTrue(CellFactory.getCellInstance(0, 0, CellType.BOTTOM_LEFT) instanceof MovableCell);
        assertTrue(CellFactory.getCellInstance(0, 0, CellType.BOTTOM_RIGHT) instanceof MovableCell);
        assertTrue(CellFactory.getCellInstance(0, 0, CellType.HORIZONTAL) instanceof MovableCell);
        assertTrue(CellFactory.getCellInstance(0, 0, CellType.VERTICAL) instanceof MovableCell);
        assertTrue(CellFactory.getCellInstance(0, 0, CellType.FREE) instanceof MovableCell);
        assertTrue(CellFactory.getCellInstance(0, 0, CellType.ROTATABLE_VERTICAL) instanceof RotatableCell);
        assertTrue(CellFactory.getCellInstance(0, 0, CellType.ROTATABLE_HORIZONTAL) instanceof RotatableCell);
        assertTrue(CellFactory.getCellInstance(0, 0, CellType.RIVER) instanceof Cell
                && !(CellFactory.getCellInstance(0, 0, CellType.RIVER) instanceof MovableCell)
                && !(CellFactory.getCellInstance(0, 0, CellType.RIVER) instanceof RotatableCell));
        assertTrue(CellFactory.getCellInstance(0, 0, CellType.MOUNTAINS) instanceof Cell
                && !(CellFactory.getCellInstance(0, 0, CellType.MOUNTAINS) instanceof MovableCell)
                && !(CellFactory.getCellInstance(0, 0, CellType.MOUNTAINS) instanceof RotatableCell));
        assertTrue(CellFactory.getCellInstance(0, 0, CellType.START) instanceof Cell
                && !(CellFactory.getCellInstance(0, 0, CellType.START) instanceof MovableCell)
                && !(CellFactory.getCellInstance(0, 0, CellType.START) instanceof RotatableCell));
        assertTrue(CellFactory.getCellInstance(0, 0, CellType.FINISH) instanceof Cell
                && !(CellFactory.getCellInstance(0, 0, CellType.FINISH) instanceof MovableCell)
                && !(CellFactory.getCellInstance(0, 0, CellType.FINISH) instanceof RotatableCell));
    }
}
