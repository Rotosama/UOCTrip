package edu.uoc.trip.model.utils;

import org.junit.jupiter.api.*;
import org.junit.platform.commons.support.ModifierSupport;

import java.lang.reflect.Modifier;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.*;

@TestInstance(PER_CLASS)
class CoordinateTest {

    private Coordinate c1;
    private Coordinate c2;
    private final Class<Coordinate> ownClass = Coordinate.class;

    @BeforeAll
    void setUp(){
        c1 = new Coordinate(1,2);
        c2 = new Coordinate(-10,-2);
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity - Class definition")
    void checkClassSanity() {
        int modifiers = ownClass.getModifiers();
        assertTrue(Modifier.isPublic(modifiers));
        assertFalse(Modifier.isStatic(modifiers));
        assertFalse(Modifier.isFinal(modifiers));

        assertEquals("edu.uoc.trip.model.utils",ownClass.getPackageName());
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
        assertEquals(5,Arrays.stream(ownClass.getDeclaredMethods()).filter(p -> Modifier.isPublic(p.getModifiers())).count());
        //Max 0 protected methods
        assertEquals(0,Arrays.stream(ownClass.getDeclaredMethods()).filter(p -> Modifier.isProtected(p.getModifiers())).count());
        //Max 0 package-private methods
        assertEquals(0,Arrays.stream(ownClass.getDeclaredMethods()).filter(p -> Modifier.isNative(p.getModifiers())).count());
        //Min 2 private methods
        assertTrue(Arrays.stream(ownClass.getDeclaredMethods()).filter(ModifierSupport::isPrivate).count()>=2);

        try {
            //These methods must be private
            assertTrue(Modifier.isPrivate(ownClass.getDeclaredMethod("setRow", int.class).getModifiers()));
            assertTrue(Modifier.isPrivate(ownClass.getDeclaredMethod("setColumn", int.class).getModifiers()));


            //Max 1 constructor
            assertEquals(1,ownClass.getDeclaredConstructors().length);

            //Constructor
            int modifiers = ownClass.getDeclaredConstructor(int.class,int.class).getModifiers();
            assertTrue(Modifier.isPublic(modifiers));

        } catch (NoSuchMethodException e) {
            fail("There is some problem with the definition of Coordinate's methods/constructors. Please read the PRAC 2 - Statement:\n");
            e.printStackTrace();
        }
    }


    @Test
    @Tag("minimum")
    @DisplayName("Minimum - getRow")
    void getRow() {
        assertEquals(1,c1.getRow());
        assertEquals(-10,c2.getRow());
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - getColumn")
    void getColumn() {
        assertEquals(2,c1.getColumn());
        assertEquals(-2,c2.getColumn());
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - equals")
    void testEquals() {
        assertFalse(c1.equals(c2));
        assertFalse(c2.equals(c1));

        assertFalse(c1.equals(null));
        assertFalse(c2.equals(null));

        assertFalse(c1.equals(new String("Eps!")));

        assertTrue(c1.equals(c1));

        Coordinate c3 = new Coordinate(c1.getRow(),c1.getColumn());
        assertTrue(c1.equals(c3));
        assertTrue(c3.equals(c1));
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - hashCode")
    void testHashCode() {
        assertNotEquals(c1.hashCode(),c2.hashCode());

        Coordinate c3 = new Coordinate(c1.getRow(),c1.getColumn());
        assertEquals(c1.hashCode(),c3.hashCode());
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - toString")
    void testToString() {
        assertEquals("(1,2)",c1.toString());
        assertEquals("(-10,-2)",c2.toString());
    }
}
