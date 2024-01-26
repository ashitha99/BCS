package com.lms.bcs.Entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "copies")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Copy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(nullable = false)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CopyStatus status; // Enum defining the statuses like Available, Reserved, Damaged, etc.

    // Constructors

    // Getters and Setters

    // Equals and HashCode

    // Other methods and logic

    public enum CopyStatus {
        AVAILABLE,
        RESERVED,
        DAMAGED
        // Add other statuses as required
    }
}

