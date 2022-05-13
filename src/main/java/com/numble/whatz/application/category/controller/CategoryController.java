package com.numble.whatz.application.category.controller;

import com.numble.whatz.application.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("cate/all")
    public List<String> findAllCategory() {
        return categoryService.findAll();
    }
}
