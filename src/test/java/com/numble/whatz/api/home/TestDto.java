package com.numble.whatz.api.home;

import lombok.*;

@Data
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
