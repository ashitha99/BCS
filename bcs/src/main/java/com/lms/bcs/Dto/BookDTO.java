package com.lms.bcs.Dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
    private String title;
    private Long authorId;
    private String ISBN;
    private Long genreId;
    private Long publisherId;
    private Date publicationDate;
    private String summary;
    private String coverImage;
    private Double rating;
    private String status;

}
