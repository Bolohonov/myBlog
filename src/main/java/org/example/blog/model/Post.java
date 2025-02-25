package org.example.blog.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class Post {

    private Long id;
    private String title;
    private String content;
    private byte[] image;
    private LocalDateTime created;
    private LocalDateTime updated;
    private Set<Comment> comments = new LinkedHashSet<>();
    private Set<Like> likes = new LinkedHashSet<>();
    private Set<Tag> tags = new LinkedHashSet<>();
}
