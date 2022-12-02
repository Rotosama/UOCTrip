package edu.uoc.trip.model.levels;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.platform.commons.support.ModifierSupport;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
class DirectionTest {

    private final Class<Direction> ownClass = Direction.class;

    @Test
    @Tag("sanity")
    @DisplayName("Sanity - Enum definition")
    void checkEnumSanity() {
        assertTrue(ownClass.isEnum());
        int modifiers = ownClass.getModifiers();
        assertTrue(Modifier.isPublic(modifiers));

        assertEquals("edu.uoc.trip.model.levels",ownClass.getPackageName());
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity - Fields definition")
    void checkFieldsSanity() {
        //Minimum number of fields
        assertTrue(ownClass.getDeclaredFields().length >= 7);
        //Max 4 public fields
        assertEquals(4, Arrays.stream(ownClass.getDeclaredFields()).filter(ModifierSupport::isPublic).count());
        //Max 0 protected fields
        assertEquals(0, Arrays.stream(ownClass.getDeclaredFields()).filter(p -> Modifier.isProtected(p.getModifiers())).count());
        //Max 0 package-private fields
        assertEquals(0, Arrays.stream(ownClass.getDeclaredFields())
                .filter(p -> !Modifier.isPublic(p.getModifiers())
                        && !Modifier.isProtected(p.getModifiers())
                        && !Modifier.isPrivate(p.getModifiers())).count());
        //Minimum 5 private fields. There is an implicit private static $VALUES field
        assertTrue(Arrays.stream(ownClass.getDeclaredFields()).filter(ModifierSupport::isPrivate).count() >= 4);
        //Max 4 Enum values
        assertEquals(4, Arrays.stream(ownClass.getDeclaredFields()).filter(Field::isEnumConstant).count());
        //Max 5 static values
        assertEquals(5, Arrays.stream(ownClass.getDeclaredFields()).filter(ModifierSupport::isStatic).count());
        //All fields must be final
        assertTrue(Arrays.stream(ownClass.getDeclaredFields()).filter(p ->
                Modifier.isFinal(p.getModifiers())).count() >= 7
        );
        //Min 3 int fields
        assertTrue(Arrays.stream(ownClass.getDeclaredFields()).filter(p -> p.getType().getName().equals("int")).count() >= 3);
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity - Methods and Constructors definition")
    void checkMethodsSanity() {
        //Min 4 methods
        assertTrue(ownClass.getDeclaredMethods().length>=4);
        //Max 4 public methods + 2 implicit public methods (values() and valueOf()).
        assertEquals(6,Arrays.stream(ownClass.getDeclaredMethods()).filter(ModifierSupport::isPublic).count());
        //Max 0 protected methods
        assertEquals(0,Arrays.stream(ownClass.getDeclaredMethods()).filter(p -> Modifier.isProtected(p.getModifiers())).count());
        //Max 0 package-private methods
        assertEquals(0,Arrays.stream(ownClass.getDeclaredMethods())
                .filter(p -> !Modifier.isPublic(p.getModifiers())
                        && !Modifier.isProtected(p.getModifiers())
                        && !Modifier.isPrivate(p.getModifiers())).count());

        //Max 1 constructor
        assertEquals(1,ownClass.getDeclaredConstructors().length);

        try {
            int constructorModifiers = ownClass.getDeclaredConstructor(String.class,int.class,int.class,int.class,int.class).getModifiers();
            assertTrue(Modifier.isPrivate(constructorModifiers));
            assertFalse(Modifier.isStatic(constructorModifiers));
            assertFalse(Modifier.isFinal(constructorModifiers));
        } catch (NoSuchMethodException e) {
            fail("Direction's constructor is defined wrongly");
            e.printStackTrace();
        }
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - getValueByIndex")
    void getValueByIndex() {
        assertEquals(Direction.UP,Direction.getValueByIndex(0));
        assertEquals(Direction.RIGHT,Direction.getValueByIndex(1));
        assertEquals(Direction.DOWN,Direction.getValueByIndex(2));
        assertEquals(Direction.LEFT,Direction.getValueByIndex(3));
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - getDRow")
    void getDRow() {
        assertEquals(0,Direction.LEFT.getDRow());
        assertEquals(-1,Direction.UP.getDRow());
        assertEquals(0,Direction.RIGHT.getDRow());
        assertEquals(1,Direction.DOWN.getDRow());
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - getDColumn")
    void getDColumn() {
        assertEquals(-1,Direction.LEFT.getDColumn());
        assertEquals(0,Direction.UP.getDColumn());
        assertEquals(1,Direction.RIGHT.getDColumn());
        assertEquals(0,Direction.DOWN.getDColumn());
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - getOpposite")
    void getOpposite() {
        assertEquals(Direction.RIGHT,Direction.LEFT.getOpposite());
        assertEquals(Direction.DOWN,Direction.UP.getOpposite());
        assertEquals(Direction.LEFT,Direction.RIGHT.getOpposite());
        assertEquals(Direction.UP,Direction.DOWN.getOpposite());
    }
}
