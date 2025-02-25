package org.example.blog.service;

import lombok.RequiredArgsConstructor;
import org.example.blog.api.mapper.TagMapper;
import org.example.blog.api.response.TagResponse;
import org.example.blog.model.Tag;
import org.example.blog.repo.TagRepo;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepo tagRepo;

    public Set<Tag> save(String tags) {
        Set<String> tagsFrom = Arrays.stream(tags.split(","))
                .map(String::trim)
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
        return new HashSet<>(tagRepo.getOrCreate(tagsFrom));
    }
}
