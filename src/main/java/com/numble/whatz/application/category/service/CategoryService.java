package com.numble.whatz.application.category.service;

import com.numble.whatz.application.category.domain.Category;
import com.numble.whatz.application.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<String> findAll() {
        return categoryRepository.findAll().stream().map(Category::getName).collect(Collectors.toList());
    }
}
