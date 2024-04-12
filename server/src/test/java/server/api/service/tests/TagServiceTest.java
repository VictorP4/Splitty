package server.api.service.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import server.api.services.TagService;
import server.database.TagRepository;
import commons.Tag;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link TagService} class.
 */
public class TagServiceTest {

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagService tagService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests the {@link TagService#createTag(String, int, int, int)} method.
     * Verifies that a tag is successfully created with the provided name and color.
     */
    @Test
    public void createTagValidInput() {
        Tag tag = new Tag("Test", 100, 150, 200);
        when(tagRepository.save(any(Tag.class))).thenReturn(tag);

        Tag createdTag = tagService.createTag("Test", 100, 150, 200);

        assertNotNull(createdTag);
        assertEquals("Test", createdTag.getName());
        assertEquals(100, createdTag.getRed());
        assertEquals(150, createdTag.getGreen());
        assertEquals(200, createdTag.getBlue());
    }

    /**
     * Tests the {@link TagService#getAllTags()} method.
     * Verifies that a list of all tags is returned from the repository.
     */
    @Test
    public void getAllTagsRepositoryReturnsTags() {
        Tag tag1 = new Tag("Tag1", 100, 150, 200);
        Tag tag2 = new Tag("Tag2", 50, 75, 100);
        List<Tag> tags = Arrays.asList(tag1, tag2);
        when(tagRepository.findAll()).thenReturn(tags);

        List<Tag> result = tagService.getAllTags();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Tag1", result.get(0).getName());
        assertEquals("Tag2", result.get(1).getName());
    }

    /**
     * Tests the {@link TagService#getTagById(Long)} method.
     * Verifies that a tag is retrieved by its ID if it exists.
     */
    @Test
    public void getTagByIdTagExists() {
        Tag tag = new Tag("Test", 100, 150, 200);
        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));

        Optional<Tag> result = tagService.getTagById(1L);

        assertTrue(result.isPresent());
        assertEquals("Test", result.get().getName());
        assertEquals(100, result.get().getRed());
        assertEquals(150, result.get().getGreen());
        assertEquals(200, result.get().getBlue());
    }

    /**
     * Tests the {@link TagService#getTagById(Long)} method.
     * Verifies that an empty optional is returned when no tag is found with the given ID.
     */
    @Test
    public void getTagByIdTagDoesNotExist() {
        when(tagRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Tag> result = tagService.getTagById(1L);

        assertTrue(result.isEmpty());
    }

    /**
     * Tests the updateTag method in tagService
     */
    @Test
    public void testUpdateTagPresent() {
        Tag tag = new Tag("Test", 100, 150, 200);
        tag.setId(1L);
        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));
        when(tagRepository.save(tag)).thenReturn(tag);
        tagRepository.save(tag);

        Tag updated = new Tag("update", 2, 3,4);
        updated.setId(1L);

        assertEquals(updated, tagService.updateTag(1L, "update", 2, 3, 4));
    }

    /**
     * Tests the deleteTag method in the tag service when tag does not exist
     */
    @Test
    public void testUpdateTag() {
        assertFalse(tagService.getTagById(1L).isPresent());
        assertEquals(Optional.empty(), tagService.getTagById(1L));

        assertNull(tagService.updateTag(1L, "sure", 1, 2, 3));
    }

    /**
     * Tests the deleteTag method in the tag service when the tag exists
     */
    @Test
    public void testDeleteTagPresent() {
        Tag tag = new Tag("Test", 100, 150, 200);
        tag.setId(1L);
        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));
        when(tagRepository.save(tag)).thenReturn(tag);

        tagRepository.save(tag);
        assertTrue(tagService.getTagById(1L).isPresent());
        assertEquals(tag, tagService.getTagById(1L).get());

        assertTrue(tagService.deleteTag(1L));
    }

    /**
     * Tests the deleteTag method in the tag service when tag does not exist
     */
    @Test
    public void testDeleteTag() {
        assertFalse(tagService.getTagById(1L).isPresent());
        assertEquals(Optional.empty(), tagService.getTagById(1L));

        assertFalse(tagService.deleteTag(1L));
    }
}
