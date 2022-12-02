package edu.uoc.trip.controller;

import edu.uoc.trip.model.levels.LevelException;
import edu.uoc.trip.model.utils.Coordinate;
import org.junit.jupiter.api.*;
import org.junit.platform.commons.support.ModifierSupport;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
class GameTest {

    private Game game;
    private final Class<Game> ownClass = Game.class;

    @BeforeEach
    void setUp() {
        try {
            game = new Game("levels/test/");
        } catch (NullPointerException | IOException e) {
            fail("setUp failed");
            e.printStackTrace();
        }
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
        assertEquals("edu.uoc.trip.controller",ownClass.getPackageName());
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity - Fields definition")
    void checkFieldsSanity() {
        //All fields must be private
        assertTrue(Arrays.stream(ownClass.getDeclaredFields()).allMatch(p -> Modifier.isPrivate(p.getModifiers())));

        try {
            int modifiers = 0;
            Field f = ownClass.getDeclaredField("maxLevels");
            modifiers = f.getModifiers();
            assertEquals("int", f.getType().getSimpleName());
            f.setAccessible(true);
            assertFalse(Modifier.isStatic(modifiers));
            assertTrue(Modifier.isFinal(modifiers));
        } catch (NoSuchFieldException e) {
            fail("There is some problem with the definition of Game's fields. Please read the PRAC 2 - Statement:\n");
            e.printStackTrace();
        }
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity - Methods definition")
    void checkMethodsSanity() {
        //Min 16 methods
        assertTrue(ownClass.getDeclaredMethods().length>=16);
        //Max 12 public methods
        assertEquals(12,Arrays.stream(ownClass.getDeclaredMethods()).filter(p -> Modifier.isPublic(p.getModifiers())).count());
        //Max 0 protected methods
        assertEquals(0,Arrays.stream(ownClass.getDeclaredMethods()).filter(p -> Modifier.isProtected(p.getModifiers())).count());
        //Max 0 package-private methods
        assertEquals(0,Arrays.stream(ownClass.getDeclaredMethods()).filter(p -> Modifier.isNative(p.getModifiers())).count());
        //Min 4 private methods
        assertTrue(Arrays.stream(ownClass.getDeclaredMethods()).filter(ModifierSupport::isPrivate).count()>=4);

        try {
            //These methods must be private
            assertTrue(Modifier.isPrivate(ownClass.getDeclaredMethod("getFileFolder").getModifiers()));
            assertTrue(Modifier.isPrivate(ownClass.getDeclaredMethod("setFileFolder", String.class).getModifiers()));
            assertTrue(Modifier.isPrivate(ownClass.getDeclaredMethod("isFinished").getModifiers()));
            assertTrue(Modifier.isPrivate(ownClass.getDeclaredMethod("loadLevel").getModifiers()));

            //Max 1 constructor
            assertEquals(1,ownClass.getDeclaredConstructors().length);

            //Constructor
            int modifiers = ownClass.getDeclaredConstructor(String.class).getModifiers();
            assertTrue(Modifier.isPublic(modifiers));

        } catch (NoSuchMethodException e) {
            fail("There is some problem with the definition of Game's methods/constructors. Please read the PRAC 2 - Statement:\n");
            e.printStackTrace();
        }
    }

    @Test
    @Tag("controller")
    @DisplayName("Controller - nextLevel")
    void nextLevel() {
        try {
            assertTrue(game.nextLevel());
            assertTrue(game.nextLevel());
            assertFalse(game.nextLevel());
        } catch (LevelException e) {
            fail("nextLevel failed");
            e.printStackTrace();
        }
    }

    @Test
    @Tag("controller")
    @DisplayName("Controller - reload")
    void reload() {
        try {
            assertTrue(game.nextLevel());
            game.swap(new Coordinate(1,0),new Coordinate(2,0));
            assertEquals("1234"+System.lineSeparator()
                    +"a|v·~·"+System.lineSeparator()
                    +"b|╚╗═┃"+System.lineSeparator()
                    +"c|╔·╚╗"+System.lineSeparator()
                    +"d|·M·^", game.getBoardText().trim());
            assertEquals(1,game.getNumMoves());
            game.swap(new Coordinate(2,1),new Coordinate(2,2));
            assertEquals("1234"+System.lineSeparator()
                    +"a|v·~·"+System.lineSeparator()
                    +"b|╚╗═┃"+System.lineSeparator()
                    +"c|╔╚·╗"+System.lineSeparator()
                    +"d|·M·^", game.getBoardText().trim());
            assertEquals(2,game.getNumMoves());
            game.reload();
            assertEquals("1234"+System.lineSeparator()
                    +"a|v·~·"+System.lineSeparator()
                    +"b|╔╗═┃"+System.lineSeparator()
                    +"c|╚·╚╗"+System.lineSeparator()
                    +"d|·M·^", game.getBoardText().trim());
            assertEquals(0,game.getNumMoves());
        } catch (LevelException e) {
            fail("nextLevel failed");
            e.printStackTrace();
        }
    }
}
