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
        assertEquals(120, tag.getRed());
        assertEquals(180, tag.getGreen());
        assertEquals(220, tag.getBlue());
    }

    /**
     * Tests the getName method in the Tag class.
     * This should return the name of the tag.
     */
    @Test
    public void testGetName() {
        Tag tag = new Tag();
        tag.setName("Please give coverage");
        assertEquals("Please give coverage", tag.getName());
    }

    /**
     * Tests the setId method in the Tag class.
     * This should set the id of the tag.
     */
    @Test
    public void testSetId() {
        Tag tag = new Tag();
        tag.setId(1L);
        assertEquals(1L, tag.getId());
    }

    /**
     * Test the setColor method of the Tag class.
     */
    @Test
    public void testSetColor() {
        Tag tag = new Tag("Test Tag", 120, 180, 220);
        tag.setRed(100);
        tag.setGreen(200);
        tag.setBlue(255);
        assertEquals(100, tag.getRed());
        assertEquals(200, tag.getGreen());
        assertEquals(255, tag.getBlue());
    }

    /**
     * Test the setColor method of the Tag class below the minimum value in the range.
     */
    @Test
    public void testColorsBelowRange() {
        Tag tag = new Tag();
        tag.setRed(-20);
        tag.setBlue(-40);
        tag.setGreen(-30);
        assertEquals(0, tag.getRed());
        assertEquals(0, tag.getBlue());
        assertEquals(0, tag.getRed());
    }

    /**
     * Test the setColor method of the Tag class above the maximum value in the range.
     */
    @Test
    public void testSetColorAboveRange() {
        Tag tag = new Tag();
        tag.setRed(300);
        tag.setGreen(400);
        tag.setBlue(12000);
        assertEquals(255, tag.getRed());
        assertEquals(255, tag.getGreen());
        assertEquals(255, tag.getBlue());
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

