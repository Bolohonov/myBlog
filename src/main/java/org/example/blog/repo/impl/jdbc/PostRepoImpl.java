package org.example.blog.repo.impl.jdbc;

import org.example.blog.model.Post;
import org.example.blog.model.Tag;
import org.example.blog.repo.PostRepo;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;

@Repository
public class PostRepoImpl implements PostRepo {

    private static final String SAVE_QUERY = """
            INSERT INTO POSTS (title, content, image) VALUES (?, ?, ?) RETURNING ID
            """;
    private static final String FIND_ALL = """
            SELECT DISTINCT p.*
            FROM posts p
            LEFT JOIN post_tags pt ON p.id = pt.post_id
            LEFT JOIN tags t ON pt.tag_id = t.id
            LIMIT ? OFFSET ?
            """;

    private static final String UPDATE_QUERY = """ 
    UPDATE posts SET title = :title, content = :content, image = :image WHERE id = :postId
    """;
    private static final String SAVE_POST_TAGS = """
    INSERT INTO post_tags (post_id, tag_id) VALUES (?, ?)
    """;

    private final NamedParameterJdbcTemplate template;

    public PostRepoImpl(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    @Override
    public void save(Post post) {

    }

    @Override
    public Post getById(Long id) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    @Transactional
    public void update(Post post) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("title", post.getTitle())
                .addValue("content", post.getContent())
                .addValue("image", post.getImage())
                .addValue("id", post.getId());
        template.update(
                UPDATE_QUERY, params
        );
        try {

        } catch() {

        }
        template.batchUpdate(
                SAVE_POST_TAGS,
                post.getTags(),
                50,
                (PreparedStatement ps, Tag tag) -> {
                    ps.setLong(1, post.getId());
                    ps.setLong(2, tag.getId());
                }
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Post> findAll(Pageable pageable) {
        int limit = pageable.getPageSize();
        int offset = pageable.getPageNumber() * limit;

        try {

        } catch(EmptyResultDataAccessException exp) {
        }
    }
}
