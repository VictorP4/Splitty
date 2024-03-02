package server.api.controllers;

import commons.Tag;
import server.api.services.TagServiceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final TagServiceImpl tagServiceImpl;

    /**
     * Constructs a new TagController with the specified TagService.
     *
     * @param tagServiceImpl the TagService to use for tag management
     */
    public TagController(TagServiceImpl tagServiceImpl) {
        this.tagServiceImpl = tagServiceImpl;
    }

    /**
     * Creates a new tag with the specified name and color.
        *
        * @param tag the tag to create
        * @return a ResponseEntity containing the created tag and HTTP status code 201 (Created)
        */
    @PostMapping(path={"/",""})
    public ResponseEntity<Tag> createTag(@RequestBody Tag tag) {
        Tag createdTag = tagServiceImpl.createTag(tag.getName(), tag.getRed(), tag.getGreen(), tag.getBlue());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTag);
    }

    /**
     * Retrieves all tags.
     *
     * @return a ResponseEntity containing a list of all tags and HTTP status code 200 (OK)
     */
    @GetMapping(path={"/",""})
    public ResponseEntity<List<Tag>> getAllTags() {
        List<Tag> tags = tagServiceImpl.getAllTags();
        return ResponseEntity.ok(tags);
    }

    /**
     * Retrieves a tag by its ID.
     *
     * @param id the ID of the tag to retrieve
     * @return a ResponseEntity containing the retrieved tag and HTTP status code 200 (OK) if found,
     * or HTTP status code 404 (Not Found) if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Tag> getTagById(@PathVariable Long id) {
        Optional<Tag> tag = tagServiceImpl.getTagById(id);
        return tag.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Updates a tag with the specified ID, name, and color.
     *
     * @param tag the tag to update
     * @return a ResponseEntity with HTTP status code 200 (OK) if the tag was updated successfully,
     * or HTTP status code 404 (Not Found) if the tag was not found
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTag(@PathVariable Long id
                                          @RequestBody Tag tag) {
        boolean updated = tagServiceImpl.updateTag(tag.getId(), tag.getName(), tag.getRed(), tag.getGreen(), tag.getBlue());
        return updated ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    /**
     * Deletes a tag by its ID.
     *
     * @param id the ID of the tag to delete
     * @return a ResponseEntity with HTTP status code 204 (No Content) if the tag was deleted successfully,
     * or HTTP status code 404 (Not Found) if the tag was not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        boolean deleted = tagServiceImpl.deleteTag(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}

