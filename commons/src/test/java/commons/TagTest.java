package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the Tag class.
 */
public class TagTest {

    /**
     * Test the constructor and getters of the Tag class.
     */
    @Test
    public void testConstructorAndGetters() {
        Tag tag = new Tag("Test Tag", 120, 180, 220);
        assertEquals("Test Tag", tag.getName());
        assertArrayEquals(new int[]{120, 180, 220}, tag.getColor());
    }

    /**
     * Test the setColor method of the Tag class.
     */
    @Test
    public void testSetColor() {
        Tag tag = new Tag("Test Tag", 120, 180, 220);
        tag.setColor(50, 75, 100);
        assertArrayEquals(new int[]{50, 75, 100}, tag.getColor());
    }

    /**
     * Test the setColor method of the Tag class out of range values.
     */
    @Test
    public void testSetColorOutOfRange() {
        Tag tag = new Tag("Test Tag", 120, 180, 220);
        tag.setColor(300, -50, 500);
        assertArrayEquals(new int[]{255, 0, 255}, tag.getColor());
    }

    /**
     * Test the equals and hashCode methods of the Tag class.
     */
    @Test
    public void testEqualsAndHashCode() {
        Tag tag1 = new Tag("Test Tag", 120, 180, 220);
        Tag tag2 = new Tag("Test Tag", 120, 180, 220);
        Tag tag3 = new Tag("Different Tag", 120, 180, 220);

        assertEquals(tag1, tag2);
        assertNotEquals(tag1, tag3);
        assertEquals(tag1.hashCode(), tag2.hashCode());
        assertNotEquals(tag1.hashCode(), tag3.hashCode());
    }

    /**
     * Test the toString method of the Tag class.
     */
    @Test
    public void testToString() {
        Tag tag = new Tag("Test Tag", 120, 180, 220);
        assertEquals("Tag{id=null, name='Test Tag', red=120, green=180, blue=220}", tag.toString());
    }
}

