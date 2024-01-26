package com.lms.bcs.Entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.persistence.*;

@Entity
@Table(name = "authors")
@Getter
@Setter
public class Author {
@Id
@GeneratedValue(strategy = GenerationType.AUTO)
private Long id;

@Column(nullable = false)
private String name;

@Column(length = 1000)
private String biography;

@Column(name = "profile_image")
private String profileImage;
}
