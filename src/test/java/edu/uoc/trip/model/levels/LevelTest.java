package edu.uoc.trip.model.levels;

import edu.uoc.trip.model.cells.Cell;
import edu.uoc.trip.model.cells.CellType;
import edu.uoc.trip.model.cells.MovableCell;
import edu.uoc.trip.model.cells.RotatableCell;
import edu.uoc.trip.model.utils.Coordinate;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.platform.commons.support.ModifierSupport;

import java.io.BufferedReader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
class LevelTest {

    private Level level;
    private final Class<Level> ownClass = Level.class;

    @BeforeEach
    void setUp() {
        try {
            level = new Level("levels/test/level1.txt");
        } catch (LevelException e) {
            fail("setUp failed");
            e.printStackTrace();
        }
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity - Class definition")
    public void checkClassSanity() {
        int modifiers = ownClass.getModifiers();
        assertTrue(Modifier.isPublic(modifiers));
        assertFalse(Modifier.isStatic(modifiers));
        assertFalse(Modifier.isFinal(modifiers));

        assertEquals("edu.uoc.trip.model.levels",ownClass.getPackageName());
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity - Fields definition")
    void checkFieldsSanity() {
        //All fields must be private
        assertTrue(Arrays.stream(ownClass.getDeclaredFields()).allMatch(p -> Modifier.isPrivate(p.getModifiers())));

        //Min 6 private fields.
        assertTrue(ownClass.getDeclaredFields().length>=5);

        try {
            int modifiers;
            Field f = ownClass.getDeclaredField("MINIMUM_BOARD_SIZE");
            modifiers = f.getModifiers();
            assertEquals("int", f.getType().getSimpleName());
            f.setAccessible(true);
            assertEquals(3, f.get(level));
            assertTrue(Modifier.isStatic(modifiers));
            assertTrue(Modifier.isFinal(modifiers));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("There is some problem with the definition of Level's fields. Please read the PRAC 2 - Statement:\n");
            e.printStackTrace();
        }

    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity - Methods definition")
    void checkMethodsSanity() {
        //Min 17 methods
        assertTrue(ownClass.getDeclaredMethods().length>=17);
        //Max 8 public methods
        assertEquals(8,Arrays.stream(ownClass.getDeclaredMethods()).filter(p -> Modifier.isPublic(p.getModifiers())).count());
        //Max 0 protected methods
        assertEquals(0,Arrays.stream(ownClass.getDeclaredMethods()).filter(p -> Modifier.isProtected(p.getModifiers())).count());
        //Max 0 package-private methods
        assertEquals(0,Arrays.stream(ownClass.getDeclaredMethods()).filter(p -> Modifier.isNative(p.getModifiers())).count());
        //Min 7 private methods
        assertTrue(Arrays.stream(ownClass.getDeclaredMethods()).filter(ModifierSupport::isPrivate).count()>=7);

        try {
            //These methods must be private
            assertTrue(Modifier.isPrivate(ownClass.getDeclaredMethod("setSize", int.class).getModifiers()));
            assertTrue(Modifier.isPrivate(ownClass.getDeclaredMethod("setDifficulty", LevelDifficulty.class).getModifiers()));
            assertTrue(Modifier.isPrivate(ownClass.getDeclaredMethod("setNumMoves", int.class).getModifiers()));
            assertTrue(Modifier.isPrivate(ownClass.getDeclaredMethod("parse",String.class).getModifiers()));
            assertTrue(Modifier.isPrivate(ownClass.getDeclaredMethod("getFirstNonEmptyLine", BufferedReader.class).getModifiers()));
            assertTrue(Modifier.isPrivate(ownClass.getDeclaredMethod("validatePosition", Coordinate.class).getModifiers()));
            assertTrue(Modifier.isPrivate(ownClass.getDeclaredMethod("setCell", Coordinate.class, Cell.class).getModifiers()));

            //Max 1 constructor
            assertEquals(1,ownClass.getDeclaredConstructors().length);

            //Constructor
            int modifiers = ownClass.getDeclaredConstructor(String.class).getModifiers();
            assertTrue(Modifier.isPublic(modifiers));

        } catch (NoSuchMethodException e) {
            fail("There is some problem with the definition of Level's methods/constructors. Please read the PRAC 2 - Statement:\n");
            e.printStackTrace();
        }
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - Constructor exceptions")
    void testConstructorException(){
        LevelException ex = assertThrows(LevelException.class, () -> new Level("levels/errors/level-error-no-rows.txt"));
        assertEquals(LevelException.ERROR_PARSING_LEVEL_FILE, ex.getMessage());

        ex = assertThrows(LevelException.class, () -> new Level("levels/errors/level-error-no-starting.txt"));
        assertEquals(LevelException.ERROR_NO_STARTING, ex.getMessage());

        ex = assertThrows(LevelException.class, () -> new Level("levels/errors/level-error-no-finish.txt"));
        assertEquals(LevelException.ERROR_NO_FINISH, ex.getMessage());

        ex = assertThrows(LevelException.class, () -> new Level("levels/errors/level-error-no-road.txt"));
        assertEquals(LevelException.ERROR_NO_ROAD, ex.getMessage());
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - getSize")
    void getSize() {
        assertEquals(4, level.getSize());
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - getDifficulty")
    void getDifficulty() {
        assertEquals(LevelDifficulty.STARTER, level.getDifficulty());
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - getNumMoves")
    void getNumMoves() {
        try {
            MovableCell cellMovable1 = (MovableCell) level.getCell(new Coordinate(1, 0));
            MovableCell cellMovable2 = (MovableCell) level.getCell(new Coordinate(2, 2));

            assertEquals(0, level.getNumMoves());
            level.swapCells(cellMovable1.getCoordinate(), cellMovable2.getCoordinate());
            assertEquals(1, level.getNumMoves());

            level.swapCells(cellMovable2.getCoordinate(), cellMovable1.getCoordinate());
            assertEquals(2, level.getNumMoves());

            level.swapCells(cellMovable1.getCoordinate(), cellMovable2.getCoordinate());
            assertEquals(3, level.getNumMoves());

            level.rotateCell(new Coordinate(1, 3));
            assertEquals(4, level.getNumMoves());

        } catch (LevelException e) {
            fail("testNumVotes failed");
            e.printStackTrace();
        }
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - getCell")
    void getCell() {
        try {
            assertEquals(CellType.TOP_RIGHT, level.getCell(new Coordinate(2,2)).getType());
            assertEquals(CellType.FINISH, level.getCell(new Coordinate(0,0)).getType());
            assertEquals(CellType.START, level.getCell(new Coordinate(3,3)).getType());
            assertEquals(CellType.HORIZONTAL, level.getCell(new Coordinate(1,2)).getType());
            assertEquals(CellType.ROTATABLE_VERTICAL, level.getCell(new Coordinate(1,3)).getType());

            LevelException ex = assertThrows(LevelException.class, () -> level.getCell(new Coordinate(3,4)));
            assertEquals(LevelException.ERROR_COORDINATE, ex.getMessage());

            ex = assertThrows(LevelException.class, () -> level.getCell(new Coordinate(4,3)));
            assertEquals(LevelException.ERROR_COORDINATE, ex.getMessage());

            ex = assertThrows(LevelException.class, () -> level.getCell(new Coordinate(-1,0)));
            assertEquals(LevelException.ERROR_COORDINATE, ex.getMessage());

        } catch (LevelException e) {
            fail("testGetCell failed");
            e.printStackTrace();
        }
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - swapCells")
    void swapCells() {
        try {
            Cell cellStart = level.getCell(new Coordinate(0, 0));
            RotatableCell cellRotatable = (RotatableCell) level.getCell(new Coordinate(1, 3));
            MovableCell cellMovable1 = (MovableCell) level.getCell(new Coordinate(1, 0));
            MovableCell cellMovable2 = (MovableCell) level.getCell(new Coordinate(2, 2));

            LevelException ex = assertThrows(LevelException.class, () ->
                    level.swapCells(cellStart.getCoordinate(), cellMovable1.getCoordinate()));
            assertEquals(LevelException.ERROR_NO_MOVABLE_CELL, ex.getMessage());

            ex = assertThrows(LevelException.class, () -> level.swapCells(cellMovable1.getCoordinate(), cellRotatable.getCoordinate()));
            assertEquals(LevelException.ERROR_NO_MOVABLE_CELL, ex.getMessage());

            ex = assertThrows(LevelException.class, () -> level.swapCells(cellStart.getCoordinate(), cellRotatable.getCoordinate()));
            assertEquals(LevelException.ERROR_NO_MOVABLE_CELL, ex.getMessage());

            assertEquals("(1,0)", cellMovable1.getCoordinate().toString());
            assertEquals("(2,2)", cellMovable2.getCoordinate().toString());

            level.swapCells(cellMovable1.getCoordinate(), cellMovable2.getCoordinate());

            assertEquals("(2,2)", cellMovable1.getCoordinate().toString());
            assertEquals("(1,0)", cellMovable2.getCoordinate().toString());

        } catch (LevelException e) {
            fail("testSwapCells failed");
            e.printStackTrace();
        }
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - rotateCell")
    void rotateCell() {
        try {
            Cell cellRotatable = level.getCell(new Coordinate(1, 3));
            Cell cellMovable = level.getCell(new Coordinate(1, 0));

            LevelException ex = assertThrows(LevelException.class, () -> level.rotateCell(cellMovable.getCoordinate()));
            assertEquals(LevelException.ERROR_NO_ROTATABLE_CELL, ex.getMessage());

            assertEquals(CellType.ROTATABLE_VERTICAL, cellRotatable.getType());
            level.rotateCell(cellRotatable.getCoordinate());
            assertEquals(CellType.ROTATABLE_HORIZONTAL, cellRotatable.getType());
        } catch (LevelException e) {
            fail("testRotateCell failed");
            e.printStackTrace();
        }
    }

    @Test
    @Tag("advanced")
    @DisplayName("Advanced - isSolvedLevel0")
    void isSolvedLevel1() {
        try {
            assertFalse(level.isSolved());
            level.swapCells(new Coordinate(1,0),new Coordinate(2,0));
            assertFalse(level.isSolved());
            level.swapCells(new Coordinate(2,1),new Coordinate(2,2));
            assertFalse(level.isSolved());
            level.swapCells(new Coordinate(1,2),new Coordinate(2,2));
            assertTrue(level.isSolved());
        } catch (LevelException e) {
            fail("testIsSolved failed");
            e.printStackTrace();
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {1,2,3,4,5,6,7,8,9})
    @Tag("advanced")
    @DisplayName("Advanced - isSolved")
    void isSolved(int id){
        try {
            level = new Level("levels/solved/level"+id+".txt");
            assertTrue(level.isSolved());
        } catch (IllegalArgumentException | LevelException e) {
            fail("isSolvedOK failed");
            e.printStackTrace();
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {1,2,3,4,5,6,7,8,9})
    @Tag("advanced")
    @DisplayName("Advanced - isUnSolved")
    void isUnsolved(int id){
        try {
            level = new Level("levels/unsolved/level"+id+".txt");
            assertFalse(level.isSolved());
        } catch (IllegalArgumentException | LevelException e) {
            fail("isSolvedOK failed");
            e.printStackTrace();
        }
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - toString")
    void testToString() {
        assertEquals("1234"+System.lineSeparator()
                +"a|v·~·"+System.lineSeparator()
                +"b|╔╗═┃"+System.lineSeparator()
                +"c|╚·╚╗"+System.lineSeparator()
                +"d|·M·^", level.toString().trim());
        try {
            level.swapCells(new Coordinate(2,0),
                    new Coordinate(1,1));
        } catch (LevelException e) {
            fail("testRotateCell failed");
            e.printStackTrace();
        }
        assertEquals("1234"+System.lineSeparator()
                +"a|v·~·"+System.lineSeparator()
                +"b|╔╚═┃"+System.lineSeparator()
                +"c|╗·╚╗"+System.lineSeparator()
                +"d|·M·^", level.toString().trim());
    }
}
