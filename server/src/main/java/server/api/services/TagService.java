package server.api.services;


import commons.Tag;

import java.util.List;
import java.util.Optional;

public interface TagService {

    /**
     * Creates a new tag with the specified name and color.
     *
     * @param name the name of the tag
     * @param red the red component of the tag's color (0-255)
     * @param green the green component of the tag's color (0-255)
     * @param blue the blue component of the tag's color (0-255)
     * @return the created tag
     */
    Tag createTag(String name, int red, int green, int blue);

    /**
     * Retrieves all tags.
     *
     * @return a list of all tags
     */
    List<Tag> getAllTags();

    /**
     * Retrieves a tag by its ID.
     *
     * @param id the ID of the tag to retrieve
     * @return an Optional containing the tag if found, or empty otherwise
     */
    Optional<Tag> getTagById(Long id);

    /**
     * Updates the name and color of a tag.
     *
     * @param id the ID of the tag to update
     * @param name the new name of the tag
     * @param red the new red component of the tag's color (0-255)
     * @param green the new green component of the tag's color (0-255)
     * @param blue the new blue component of the tag's color (0-255)
     * @return true if the tag was updated successfully, false otherwise
     */
    boolean updateTag(Long id, String name, int red, int green, int blue);

    /**
     * Deletes a tag by its ID.
     *
     * @param id the ID of the tag to delete
     * @return true if the tag was deleted successfully, false otherwise
     */
    boolean deleteTag(Long id);
}

