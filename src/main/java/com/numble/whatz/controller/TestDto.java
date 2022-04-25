package com.numble.whatz.controller;

import lombok.*;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TestDto {
    private Long id;
    private String email;

    @Builder
    public TestDto(Long id, String email) {
        this.id = id;
        this.email = email;
    }
}
