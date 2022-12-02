package edu.uoc.trip.model.cells;

import edu.uoc.trip.model.levels.Direction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.platform.commons.support.ModifierSupport;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
class CellTypeTest {

    private final Class<CellType> ownClass = CellType.class;

    @Test
    @Tag("sanity")
    @DisplayName("Sanity - Enum definition")
    void checkEnumSanity() {
        assertTrue(ownClass.isEnum());
        int modifiers = ownClass.getModifiers();
        assertTrue(Modifier.isPublic(modifiers));
        assertEquals("edu.uoc.trip.model.cells",ownClass.getPackageName());
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity - Fields definition")
    void checkFieldsSanity() {
        //Minimum number of fields. There is an implicit private static $VALUES field
        assertTrue(ownClass.getDeclaredFields().length >= 18);

        //Max 13 public fields
        assertEquals(13, Arrays.stream(ownClass.getDeclaredFields()).filter(ModifierSupport::isPublic).count());
        //Max 0 protected fields
        assertEquals(0, Arrays.stream(ownClass.getDeclaredFields()).filter(p -> Modifier.isProtected(p.getModifiers())).count());
        //Max 0 package-private fields
        assertEquals(0, Arrays.stream(ownClass.getDeclaredFields())
                .filter(p -> !Modifier.isPublic(p.getModifiers())
                        && !Modifier.isProtected(p.getModifiers())
                        && !Modifier.isPrivate(p.getModifiers())).count());
        //Minimum 5 private fields. There is an implicit private static $VALUES field
        assertTrue(Arrays.stream(ownClass.getDeclaredFields()).filter(ModifierSupport::isPrivate).count() >= 5);
        //Max 4 Enum values
        assertEquals(13, Arrays.stream(ownClass.getDeclaredFields()).filter(Field::isEnumConstant).count());
        //Max 14 static values. There is an implicit private static $VALUES field
        assertEquals(14, Arrays.stream(ownClass.getDeclaredFields()).filter(ModifierSupport::isStatic).count());

        //Min 4 char fields
        assertTrue(Arrays.stream(ownClass.getDeclaredFields()).filter(p -> p.getType().getSimpleName().equals("char")).count() >= 2);
        //Min 1 String fields
        assertTrue(Arrays.stream(ownClass.getDeclaredFields()).anyMatch(p -> p.getType().getSimpleName().equals("String")));
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity - Methods and Constructors definition")
    void checkMethodsSanity() {
        //Min 4 methods
        assertTrue(ownClass.getDeclaredMethods().length>=4);
        //Max 6 public methods + 2 implicit public methods (values() and valueOf()).
        assertEquals(8,Arrays.stream(ownClass.getDeclaredMethods()).filter(ModifierSupport::isPublic).count());
        //Max 0 protected methods
        assertEquals(0,Arrays.stream(ownClass.getDeclaredMethods()).filter(p -> Modifier.isProtected(p.getModifiers())).count());
        //Max 0 package-private methods
        assertEquals(0,Arrays.stream(ownClass.getDeclaredMethods())
                .filter(p -> !Modifier.isPublic(p.getModifiers())
                        && !Modifier.isProtected(p.getModifiers())
                        && !Modifier.isPrivate(p.getModifiers())).count());

        //Max 1 constructor
        assertEquals(1,ownClass.getDeclaredConstructors().length);

        int constructorModifiers = ownClass.getDeclaredConstructors()[0].getModifiers();
        assertTrue(Modifier.isPrivate(constructorModifiers));
        assertFalse(Modifier.isStatic(constructorModifiers));
        assertFalse(Modifier.isFinal(constructorModifiers));
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - getFileSymbol")
    void getFileSymbol() {
        assertEquals('S', CellType.START.getFileSymbol());
        assertEquals('F', CellType.FINISH.getFileSymbol());
        assertEquals('M', CellType.MOUNTAINS.getFileSymbol());
        assertEquals('~', CellType.RIVER.getFileSymbol());
        assertEquals('V', CellType.VERTICAL.getFileSymbol());
        assertEquals('H', CellType.HORIZONTAL.getFileSymbol());
        assertEquals('r', CellType.BOTTOM_RIGHT.getFileSymbol());
        assertEquals('l', CellType.BOTTOM_LEFT.getFileSymbol());
        assertEquals('R', CellType.TOP_RIGHT.getFileSymbol());
        assertEquals('L', CellType.TOP_LEFT.getFileSymbol());
        assertEquals('·', CellType.FREE.getFileSymbol());
        assertEquals('G', CellType.ROTATABLE_VERTICAL.getFileSymbol());
        assertEquals('g', CellType.ROTATABLE_HORIZONTAL.getFileSymbol());
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - getUnicodeRepresentation")
    void getUnicodeRepresentation() {
        assertEquals('^', CellType.START.getUnicodeRepresentation());
        assertEquals('v', CellType.FINISH.getUnicodeRepresentation());
        assertEquals('M', CellType.MOUNTAINS.getUnicodeRepresentation());
        assertEquals('~', CellType.RIVER.getUnicodeRepresentation());
        assertEquals('\u2551', CellType.VERTICAL.getUnicodeRepresentation());
        assertEquals('\u2550', CellType.HORIZONTAL.getUnicodeRepresentation());
        assertEquals('\u2554', CellType.BOTTOM_RIGHT.getUnicodeRepresentation());
        assertEquals('\u2557', CellType.BOTTOM_LEFT.getUnicodeRepresentation());
        assertEquals('\u255A', CellType.TOP_RIGHT.getUnicodeRepresentation());
        assertEquals('\u255D', CellType.TOP_LEFT.getUnicodeRepresentation());
        assertEquals('\u00b7', CellType.FREE.getUnicodeRepresentation());
        assertEquals('\u2503', CellType.ROTATABLE_VERTICAL.getUnicodeRepresentation());
        assertEquals('\u2501', CellType.ROTATABLE_HORIZONTAL.getUnicodeRepresentation());
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - getImageSrc")
    void getImageSrc() {
        assertEquals("start.png", CellType.START.getImageSrc());
        assertEquals("finish.png", CellType.FINISH.getImageSrc());
        assertEquals("mountains.png", CellType.MOUNTAINS.getImageSrc());
        assertEquals("river.png", CellType.RIVER.getImageSrc());
        assertEquals("road_vertical.png", CellType.VERTICAL.getImageSrc());
        assertEquals("road_horizontal.png", CellType.HORIZONTAL.getImageSrc());
        assertEquals("road_bottom_right.png", CellType.BOTTOM_RIGHT.getImageSrc());
        assertEquals("road_bottom_left.png", CellType.BOTTOM_LEFT.getImageSrc());
        assertEquals("road_top_right.png", CellType.TOP_RIGHT.getImageSrc());
        assertEquals("road_top_left.png", CellType.TOP_LEFT.getImageSrc());
        assertEquals("free.png", CellType.FREE.getImageSrc());
        assertEquals("road_rotatable_vertical.png", CellType.ROTATABLE_VERTICAL.getImageSrc());
        assertEquals("road_rotatable_horizontal.png", CellType.ROTATABLE_HORIZONTAL.getImageSrc());
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - getAvailableConnections")
    void getAvailableConnections() {
        assertEquals(EnumSet.of(Direction.UP), CellType.START.getAvailableConnections());
        assertEquals(EnumSet.of(Direction.DOWN), CellType.FINISH.getAvailableConnections());
        assertTrue(CellType.MOUNTAINS.getAvailableConnections().isEmpty());
        assertTrue(CellType.RIVER.getAvailableConnections().isEmpty());
        assertEquals(EnumSet.of(Direction.UP, Direction.DOWN), CellType.VERTICAL.getAvailableConnections());
        assertEquals(EnumSet.of(Direction.LEFT, Direction.RIGHT), CellType.HORIZONTAL.getAvailableConnections());
        assertEquals(EnumSet.of(Direction.LEFT, Direction.DOWN), CellType.BOTTOM_LEFT.getAvailableConnections());
        assertEquals(EnumSet.of(Direction.UP, Direction.RIGHT), CellType.TOP_RIGHT.getAvailableConnections());
        assertEquals(EnumSet.of(Direction.LEFT, Direction.UP), CellType.TOP_LEFT.getAvailableConnections());
        assertTrue(CellType.FREE.getAvailableConnections().isEmpty());
        assertEquals(EnumSet.of(Direction.UP, Direction.DOWN), CellType.ROTATABLE_VERTICAL.getAvailableConnections());
        assertEquals(EnumSet.of(Direction.LEFT, Direction.RIGHT), CellType.ROTATABLE_HORIZONTAL.getAvailableConnections());
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - map2CellType")
    void map2CellType() {
        assertEquals(CellType.START, CellType.map2CellType('S'));
        assertEquals(CellType.FINISH, CellType.map2CellType('F'));
        assertEquals(CellType.MOUNTAINS, CellType.map2CellType('M'));
        assertEquals(CellType.RIVER, CellType.map2CellType('~'));
        assertEquals(CellType.VERTICAL, CellType.map2CellType('V'));
        assertEquals(CellType.HORIZONTAL, CellType.map2CellType('H'));
        assertEquals(CellType.BOTTOM_RIGHT, CellType.map2CellType('r'));
        assertEquals(CellType.BOTTOM_LEFT, CellType.map2CellType('l'));
        assertEquals(CellType.TOP_RIGHT, CellType.map2CellType('R'));
        assertEquals(CellType.TOP_LEFT, CellType.map2CellType('L'));
        assertEquals(CellType.FREE, CellType.map2CellType('·'));
        assertEquals(CellType.ROTATABLE_VERTICAL, CellType.map2CellType('G'));
        assertEquals(CellType.ROTATABLE_HORIZONTAL, CellType.map2CellType('g'));
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - next")
    void next() {
        assertNull(CellType.START.next());
        assertNull(CellType.MOUNTAINS.next());
        assertNull(CellType.MOUNTAINS.next());
        assertNull(CellType.RIVER.next());
        assertEquals(CellType.HORIZONTAL, CellType.VERTICAL.next());
        assertEquals(CellType.VERTICAL, CellType.HORIZONTAL.next());
        assertEquals(CellType.BOTTOM_LEFT, CellType.BOTTOM_RIGHT.next());
        assertEquals(CellType.TOP_LEFT, CellType.BOTTOM_LEFT.next());
        assertEquals(CellType.BOTTOM_RIGHT, CellType.TOP_RIGHT.next());
        assertEquals(CellType.TOP_RIGHT, CellType.TOP_LEFT.next());
        assertNull(CellType.FREE.next());
        assertEquals(CellType.ROTATABLE_HORIZONTAL, CellType.ROTATABLE_VERTICAL.next());
        assertEquals(CellType.ROTATABLE_VERTICAL, CellType.ROTATABLE_HORIZONTAL.next());
    }
}
