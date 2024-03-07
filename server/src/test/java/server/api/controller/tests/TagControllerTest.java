package server.api.controller.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.api.controllers.TagController;
import server.api.services.TagServiceImpl;
import commons.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TagControllerTest {

    @Mock
    private TagServiceImpl tagService;

    @InjectMocks
    private TagController tagController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests the {@link TagController#createTag} method.
     */
    @Test
    public void testCreateTag() {
        Tag tag = new Tag("Test Tag", 100, 150, 200);
        when(tagService.createTag("Test Tag", 100, 150, 200)).thenReturn(tag);

        ResponseEntity<Tag> response = tagController.createTag(tag);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(tag, response.getBody());
        verify(tagService, times(1)).createTag("Test Tag", 100, 150, 200);
    }

    /**
     * Tests the {@link TagController#getAllTags} method.
     */
    @Test
    public void testGetAllTags() {
        List<Tag> tags = new ArrayList<>();
        tags.add(new Tag("Tag1", 50, 100, 150));
        tags.add(new Tag("Tag2", 75, 125, 175));
        when(tagService.getAllTags()).thenReturn(tags);

        ResponseEntity<List<Tag>> response = tagController.getAllTags();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tags, response.getBody());
        verify(tagService, times(1)).getAllTags();
    }

    /**
     * Tests the {@link TagController#getTagById} method.
     */
    @Test
    public void testGetTagById() {
        Tag tag = new Tag("Test Tag", 100, 150, 200);
        when(tagService.getTagById(1L)).thenReturn(Optional.of(tag));

        ResponseEntity<Tag> response = tagController.getTagById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tag, response.getBody());
        verify(tagService, times(1)).getTagById(1L);
    }

    /**
     * Tests the {@link TagController#updateTag} method.
     */
    @Test
    public void testUpdateTag() {
        Tag tagToUpdate = new Tag(null,0,0,0);
        tagToUpdate.setId(1L);
        tagToUpdate.setName("Updated Tag");
        tagToUpdate.setRed(100);
        tagToUpdate.setGreen(150);
        tagToUpdate.setBlue(200);

        when(tagService.updateTag(eq(1L), anyString(), anyInt(), anyInt(), anyInt())).thenReturn(any(Tag.class));

        ResponseEntity<Tag> response = tagController.updateTag(tagToUpdate.getId(), tagToUpdate);

        verify(tagService, times(1)).updateTag(eq(1L), eq("Updated Tag"), eq(100), eq(150), eq(200));
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Tests the {@link TagController#deleteTag} method.
     */
    @Test
    public void testDeleteTag() {
        when(tagService.deleteTag(1L)).thenReturn(true);

        ResponseEntity<Void> response = tagController.deleteTag(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(tagService, times(1)).deleteTag(1L);
    }
}

