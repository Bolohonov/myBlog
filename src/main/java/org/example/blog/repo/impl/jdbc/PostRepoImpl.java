package org.example.blog.repo.impl.jdbc;

import lombok.RequiredArgsConstructor;
import org.example.blog.model.Post;
import org.example.blog.model.Tag;
import org.example.blog.repo.PostRepo;
import org.example.blog.utils.SqlUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepoImpl implements PostRepo {
    private static final String SAVE_QUERY = """
            INSERT INTO POSTS (title, content, image) VALUES (?, ?, ?) RETURNING ID
            """;
    private static final String COUNT_POSTS = "SELECT COUNT(DISTINCT P.id) FROM POSTS P";
    private static final String FIND_ALL = """
            SELECT DISTINCT P.*
            FROM POSTS P
            LEFT JOIN POSTS_TAGS PT ON P.id = PT.post_id
            LEFT JOIN TAGS T ON PT.tag_id = T.id
            LIMIT ? OFFSET ?
            """;

    private static final String UPDATE_QUERY = """ 
            UPDATE posts SET title = :title, content = :content, image = :image WHERE id = :postId
            """;
    private static final String FIND_BY_ID = """
            SELECT P.*
            FROM POSTS P
            WHERE P.id = ?
            """;

    private static final String DELETE_BY_ID = "DELETE FROM POSTS P WHERE P.id = ?";

    private final JdbcTemplate template;

    @Override
    public Long saveWithoutTags(Post post) {
        try {
            return template.queryForObject(
                    SAVE_QUERY,
                    new Object[]{post.getTitle(), post.getContent(), post.getImage()},
                    Long.class
            );
        } catch (EmptyResultDataAccessException exp) {
            return null;
        }
    }

    @Override
    public Post getById(Long id) {
        return template.queryForObject(
                FIND_BY_ID,
                this::mapToPost,
                id
        );
    }

    @Override
    public void deleteById(Long id) {
        template.update(DELETE_BY_ID, id);
    }

    @Override
    public void updateWithoutTags(Post post) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("title", post.getTitle())
                .addValue("content", post.getContent())
                .addValue("image", post.getImage())
                .addValue("id", post.getId());
        template.update(
                UPDATE_QUERY, params
        );
    }

    @Override
    public Page<Post> findAll(Pageable pageable) {
        int limit = pageable.getPageSize();
        int offset = pageable.getPageNumber() * limit;

        List<Post> posts = template.query(
                FIND_ALL,
                this::mapToPost,
                limit,
                offset
        );
        Integer size;
        try {
            size = template.queryForObject(COUNT_POSTS, Integer.class);
        } catch (EmptyResultDataAccessException exp) {
            size = 0;
        }
        return new PageImpl<>(posts, pageable, size);
    }

    Post mapToPost(ResultSet rs, int rowNumber) throws SQLException {
        return new Post(
                SqlUtils.getLong(rs, "id"),
                SqlUtils.getStringOrElseEmpty(rs, "title"),
                SqlUtils.getStringOrElseEmpty(rs, "content"),
                rs.getBytes("image"),
                SqlUtils.getLocalDateTime(rs, "created"),
                SqlUtils.getLocalDateTime(rs, "updated")
        );
    }
}
