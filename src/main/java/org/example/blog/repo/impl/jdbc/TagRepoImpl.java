package org.example.blog.repo.impl.jdbc;

import lombok.RequiredArgsConstructor;
import org.example.blog.model.Tag;
import org.example.blog.repo.TagRepo;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class TagRepoImpl implements TagRepo {

    public static final String FIND_BY_NAME = """
            SELECT T.ID, T.NAME
            FROM TAGS T
            WHERE LOWER(T.NAME) IN (%s)
            """;

    private final NamedParameterJdbcTemplate template;

    @Override
    public List<Tag> save(List<Tag> tags) {
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<Tag> getOrCreate(Set<String> tagNames) {
    }

    @Override
    public List<Tag> findAll() {
        return List.of();
    }

    @Override
    public List<Tag> findByPostIds(List<Long> postIds) {
        return List.of();
    }

    private List<Tag> findByNameInIgnoreCase(Set<String> tagNames) {
    }
}
