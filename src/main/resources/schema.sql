CREATE TABLE IF NOT EXISTS posts
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY   NOT NULL,
    title  VARCHAR(255)                             NOT NULL,
    content text                                    NOT NULL,
    image text                                      ,
    created timestamp default (now() at time zone 'utc'),
    updated timestamp default (now() at time zone 'utc'),
    CONSTRAINT pk_post PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS comments
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY   NOT NULL,
    post_id BIGINT,
    content text                                    NOT NULL,
    created timestamp default (now() at time zone 'utc'),
    updated timestamp default (now() at time zone 'utc'),
    CONSTRAINT pk_comment PRIMARY KEY (id),
    CONSTRAINT FK_COMMENT_ON_POST FOREIGN KEY (post_id) REFERENCES posts (id)
);

CREATE TABLE IF NOT EXISTS likes
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY   NOT NULL,
    post_id BIGINT,
    user_id BIGINT,
    created timestamp default (now() at time zone 'utc'),
    CONSTRAINT pk_like PRIMARY KEY (id),
    CONSTRAINT FK_LIKE_ON_POST FOREIGN KEY (post_id) REFERENCES posts (id)
);

CREATE TABLE IF NOT EXISTS tags
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY   NOT NULL,
    name  VARCHAR(255)                             NOT NULL,
    CONSTRAINT PK_TAG PRIMARY KEY (id),
    CONSTRAINT UQ_TAG_NAME UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS posts_tags
(
    post_id    BIGINT NOT NULL,
    tag_id  BIGINT NOT NULL,
    CONSTRAINT PK_POSTS_TAGS PRIMARY KEY (post_id, tag_id),
    CONSTRAINT FK_POSTS_TAGS_ON_POST FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE,
    CONSTRAINT FK_POSTS_TAGS_ON_TAG FOREIGN KEY (tag_id) REFERENCES tags (id) ON DELETE CASCADE
);