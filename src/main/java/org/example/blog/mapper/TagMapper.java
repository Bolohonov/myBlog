package org.example.blog.mapper;

import org.example.blog.api.response.TagResponse;
import org.example.blog.model.Tag;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface TagMapper {
    TagResponse toResponse(Tag tag);
    List<TagResponse> toResponses(List<Tag> tags);
}
