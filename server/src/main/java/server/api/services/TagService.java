package server.api.services;

import org.springframework.stereotype.Service;
import server.database.TagRepository;
import commons.Tag;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing tags.
 */
@Service
public class TagService {

    private final TagRepository tagRepository;

    /**
     * Constructs a TagService with the specified TagRepository.
     *
     * @param tagRepository The TagRepository to use.
     */
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    /**
     * Creates a new tag with the given name and RGB color values.
     *
     * @param name  The name of the tag.
     * @param red   The red component of the tag's color.
     * @param green The green component of the tag's color.
     * @param blue  The blue component of the tag's color.
     * @return The created Tag.
     */
    public Tag createTag(String name, int red, int green, int blue) {
        Tag tag = new Tag(name, red, green, blue);
        return tagRepository.save(tag);
    }

    /**
     * Retrieves all tags.
     *
     * @return A list of all tags.
     */
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    /**
     * Retrieves a tag by its ID.
     *
     * @param id The ID of the tag to retrieve.
     * @return An Optional containing the tag, or empty if not found.
     */
    public Optional<Tag> getTagById(Long id) {
        return tagRepository.findById(id);
    }

    /**
     * Updates a tag's name and color values.
     *
     * @param id    The ID of the tag to update.
     * @param name  The new name for the tag.
     * @param red   The new red component of the tag's color.
     * @param green The new green component of the tag's color.
     * @param blue  The new blue component of the tag's color.
     * @return The updated Tag, or null if the tag with the specified ID was not found.
     */
    public Tag updateTag(Long id, String name, int red, int green, int blue) {
        Optional<Tag> optionalTag = tagRepository.findById(id);
        if (optionalTag.isPresent()) {
            Tag tag = optionalTag.get();
            tag.setName(name);
            tag.setRed(red);
            tag.setGreen(green);
            tag.setBlue(blue);

            return tagRepository.save(tag);
        }
        return null;
    }

    /**
     * Deletes a tag by its ID.
     *
     * @param id The ID of the tag to delete.
     * @return true if the tag was deleted successfully, false otherwise.
     */
    public boolean deleteTag(Long id) {
        Optional<Tag> optionalTag = tagRepository.findById(id);
        if (optionalTag.isPresent()) {
            tagRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
