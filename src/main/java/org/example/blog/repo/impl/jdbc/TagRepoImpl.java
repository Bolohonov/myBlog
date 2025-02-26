package org.example.blog.repo.impl.jdbc;

import lombok.RequiredArgsConstructor;
import org.example.blog.model.Tag;
import org.example.blog.repo.TagRepo;
import org.example.blog.utils.SqlUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class TagRepoImpl implements TagRepo {

    private static final int MAX_BATCH_SIZE = 500;

    private static final String SAVE_POST_TAGS = """
            INSERT INTO posts_tags (post_id, tag_id) VALUES (?, ?)
            """;

    private static final String SAVE_TAGS = "INSERT INTO TAGS (name) VALUES (?) RETURNING ID";

    private static final String FIND_BY_NAME_QUERY = """
            SELECT T.*
            FROM TAGS T
            WHERE LOWER(T.name) IN (%s)
            """;

    private final JdbcTemplate template;

    @Override
    public List<Tag> save(List<Tag> tags) {
        for (Tag tag : tags) {
            Long tagId;
            try {
                tagId = template.queryForObject(
                        SAVE_TAGS,
                        new Object[]{tag.getName()},
                        Long.class
                );
            } catch (EmptyResultDataAccessException exp) {
                return null;
            }
            if (tagId != null) {
                tag.setId(tagId);
            }
        }
        return tags;
    }

    @Override
    public void batchUpdateByPostId(Long postId, Set<Tag> tags) {
        final List<Long> tagIds = tags.stream().map(Tag::getId).toList();
        List<Long> ids = new ArrayList<>(tagIds);
        int i = 1;
        while (i != tagIds.size() / MAX_BATCH_SIZE + 2) {
            List<Long> finalIds = ids;
            template.batchUpdate(
                    SAVE_POST_TAGS,
                    new BatchPreparedStatementSetter() {
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            ps.setLong(1, postId);
                            ps.setLong(2, finalIds.get(i));
                        }

                        public int getBatchSize() {
                            return MAX_BATCH_SIZE;
                        }
                    });
            if (ids.size() > MAX_BATCH_SIZE) {
                ids = ids.subList(0, MAX_BATCH_SIZE);
            }
            i++;
        }
    }

    @Override
    public List<Tag> getOrCreate(Set<String> tagNames) {
        List<Tag> existedTags = this.findByNameInIgnoreCase(tagNames);
        Set<String> existedTagNames = existedTags.stream().map(Tag::getName).collect(Collectors.toSet());
        List<Tag> newTags = new ArrayList<>();
        tagNames.forEach(t -> {
            if (!existedTagNames.contains(t)) {
                Tag tag = new Tag();
                tag.setName(t);
                newTags.add(tag);
            }
        });
        this.save(newTags);
        newTags.addAll(existedTags);
        return newTags;
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
        String inSql = String.join(",", Collections.nCopies(tagNames.size(), "?"));
        List<Tag> tags = template.query(
                String.format(FIND_BY_NAME_QUERY, inSql),
                this::mapToTag,
                tagNames.toArray()
        );
        return tags.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    private Tag mapToTag(ResultSet rs, int rowNumber) throws SQLException {
        if (rs.getString("name") == null) {
            return null;
        }
        return new Tag(rs.getLong("id"), rs.getString("name"));
    }
}
