package org.example.blog.mapper;

import lombok.SneakyThrows;
import org.example.blog.controller.request.PostRequest;
import org.example.blog.controller.response.PostResponse;
import org.example.blog.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;

@Mapper
public interface PostMapper {

    PostResponse toResponse(Post post);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "likes", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    Post toPost(PostRequest request);

    @SneakyThrows
    default byte[] toBytes(MultipartFile source) {
        if (source == null) {
            return null;
        }
        return source.getBytes();
    }

    default String toBase64(byte[] source) {
        if (source == null) {
            return null;
        }
        return Base64.getEncoder().encodeToString(source);
    }
}
