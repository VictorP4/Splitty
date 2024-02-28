package server.api.service.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import server.api.services.TagServiceImpl;
import server.database.TagRepository;
import commons.Tag;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link TagServiceImpl} class.
 */
public class TagServiceImplTest {

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagServiceImpl tagService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Tests the {@link TagServiceImpl#createTag(String, int, int, int)} method.
     * Verifies that a tag is successfully created with the provided name and color.
     */
    @Test
    public void createTag_ValidInput() {
        Tag tag = new Tag("Test", 100, 150, 200);
        when(tagRepository.save(any(Tag.class))).thenReturn(tag);

        Tag createdTag = tagService.createTag("Test", 100, 150, 200);

        assertNotNull(createdTag);
        assertEquals("Test", createdTag.getName());
        assertEquals(100, createdTag.getColor()[0]);
        assertEquals(150, createdTag.getColor()[1]);
        assertEquals(200, createdTag.getColor()[2]);
    }

    /**
     * Tests the {@link TagServiceImpl#getAllTags()} method.
     * Verifies that a list of all tags is returned from the repository.
     */
    @Test
    public void getAllTags_RepositoryReturnsTags() {
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
     * Tests the {@link TagServiceImpl#getTagById(Long)} method.
     * Verifies that a tag is retrieved by its ID if it exists.
     */
    @Test
    public void getTagById_TagExists() {
        Tag tag = new Tag("Test", 100, 150, 200);
        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));

        Optional<Tag> result = tagService.getTagById(1L);

        assertTrue(result.isPresent());
        assertEquals("Test", result.get().getName());
        assertEquals(100, result.get().getColor()[0]);
        assertEquals(150, result.get().getColor()[1]);
        assertEquals(200, result.get().getColor()[2]);
    }

    /**
     * Tests the {@link TagServiceImpl#getTagById(Long)} method.
     * Verifies that an empty optional is returned when no tag is found with the given ID.
     */
    @Test
    public void getTagById_TagDoesNotExist() {
        when(tagRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Tag> result = tagService.getTagById(1L);

        assertTrue(result.isEmpty());
    }
}
