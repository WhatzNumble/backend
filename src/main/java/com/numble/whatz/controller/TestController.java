package com.numble.whatz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class TestController {

    @PostMapping("/test")
    public TestDto test(@RequestBody TestDto testDto) {
        System.out.println("testDto = " + testDto);
        System.out.println("testDto.getEmail() = " + testDto.getEmail());
        System.out.println("testDto.getId() = " + testDto.getId());
        TestDto testDto1 = TestDto.builder()
                .id(2L)
                .email("in Run Controller")
                .build();
        return testDto1;
    }

    @GetMapping("/test2")
    public Map<String, List<TestDto>> test1() {
        TestDto testDto1 = TestDto.builder()
                .id(1L)
                .email("Con1")
                .build();
        TestDto testDto2 = TestDto.builder()
                .id(2L)
                .email("Con2")
                .build();
        List<TestDto> testDtos = new ArrayList<>();
        Map<String, List<TestDto>> map = new HashMap<>();
        testDtos.add(testDto1);
        testDtos.add(testDto2);
        map.put("1", testDtos);

        return map;
    }
}
