package client.services;

import commons.Event;
import commons.Tag;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the TagService class.
 */
public class TagServiceTest {

    /**
     * Tests the getCellColor method of TagService.
     */
    @Test
    void testGetCellColor() {
        Tag tag = new Tag("TestTag", 100, 150, 200);
        TagService tagService = new TagService(null);
        String expectedColor = "-fx-background-color: rgba(100, 150, 200, 1);";
        assertEquals(expectedColor, tagService.getCellColor(tag));
    }

    /**
     * Tests the getCellBrightness method of TagService.
     */
    @Test
    void testGetCellBrightness() {
        Tag tag = new Tag("TestTag", 100, 150, 200);
        TagService tagService = new TagService(null);
        String expectedBrightness = "black";
        assertEquals(expectedBrightness, tagService.getCellBrightness(tag));

        tag = new Tag("TestTag", 20, 30, 40);
        expectedBrightness = "white";
        assertEquals(expectedBrightness, tagService.getCellBrightness(tag));
    }

    /**
     * Tests the createNewTag method of TagService.
     */
    @Test
    void testCreateNewTag() {
        Color color = Color.rgb(100, 150, 200);
        Tag newTag = TagService.createNewTag("TestTag", color);
        assertNotNull(newTag);
        assertEquals("TestTag", newTag.getName());
        assertEquals(100, newTag.getRed());
        assertEquals(150, newTag.getGreen());
        assertEquals(200, newTag.getBlue());
    }

    /**
     * Tests the doesTagNameExist method of TagService.
     */
    @Test
    void testDoesTagNameExist() {
        Event event = new Event();
        event.addTag(new Tag("Tag1", 100, 150, 200));
        event.addTag(new Tag("Tag2", 200, 100, 50));
        assertTrue(TagService.doesTagNameExist(event, "Tag1"));
        assertTrue(TagService.doesTagNameExist(event, "Tag2"));
        assertFalse(TagService.doesTagNameExist(event, "Tag3"));
    }

    /**
     * Tests the removeTag method of TagService.
     */
    @Test
    void testRemoveTag() {
        Event event = new Event();
        Tag tag1 = new Tag("Tag1", 100, 150, 200);
        Tag tag2 = new Tag("Tag2", 200, 100, 50);
        event.addTag(tag1);
        event.addTag(tag2);
        Tag removedTag = TagService.removeTag(event, "Tag1");
        assertEquals(tag1, removedTag);
    }
}
