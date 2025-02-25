package org.example.blog.repo;

import org.example.blog.model.Tag;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface TagRepo {
    List<Tag> save(List<Tag> tags);
    List<Tag> getOrCreate(Set<String> tagNames);
}
