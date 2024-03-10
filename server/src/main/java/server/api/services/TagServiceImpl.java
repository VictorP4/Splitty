package server.api.services;

import org.springframework.stereotype.Service;
import server.database.TagRepository;
import commons.Tag;

import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public Tag createTag(String name, int red, int green, int blue) {
        Tag tag = new Tag(name, red, green, blue);
        return tagRepository.save(tag);
    }

    @Override
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    @Override
    public Optional<Tag> getTagById(Long id) {
        return tagRepository.findById(id);
    }

    @Override
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

    @Override
    public boolean deleteTag(Long id) {
        Optional<Tag> optionalTag = tagRepository.findById(id);
        if (optionalTag.isPresent()) {
            tagRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

