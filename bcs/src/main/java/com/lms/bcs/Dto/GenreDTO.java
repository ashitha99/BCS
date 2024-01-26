package com.lms.bcs.Dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GenreDTO {
    private String name;
    private String description;
}
