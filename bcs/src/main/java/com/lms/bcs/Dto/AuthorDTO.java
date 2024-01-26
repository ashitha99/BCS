package com.lms.bcs.Dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthorDTO {
    private String name;
    private String biography;
    private String profileImage;

    // getters and setters
}


