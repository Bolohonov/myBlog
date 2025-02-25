CREATE TABLE IF NOT EXISTS posts
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY   NOT NULL,
    title  VARCHAR(255)                             NOT NULL,
    content text                                    NOT NULL,
    image text                                      ,
    CONSTRAINT pk_post PRIMARY KEY (id)
);