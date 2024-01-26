package com.lms.bcs.Entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "books")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {
@Id
@GeneratedValue(strategy = GenerationType.AUTO)
private Long id;

@Column(nullable = false)
private  String title;

@ManyToOne(fetch = FetchType.EAGER)
@JoinColumn(name = "author_id",nullable = false)
private Author author;

@Column(nullable = false,unique = true)
private String ISBN;

@ManyToOne(fetch = FetchType.EAGER)
@JoinColumn(name = "genre_id",nullable = false)
private Genre genre;

@ManyToOne(fetch = FetchType.EAGER)
@JoinColumn(name = "publisher_id", nullable = false)
private Publisher publisher;

@Column(name = "publication_date")
private Date publicationDate;

@Column(length = 1000)
private String summary;

@Column(name = "cover_image")
private String coverImage;

private Double rating;

@Enumerated(EnumType.STRING)
private BookStatus status; // Define an Enum for the status



}
