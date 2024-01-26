package com.lms.bcs.Service;

import com.lms.bcs.Dto.BookDTO;
import com.lms.bcs.Entity.*;
import com.lms.bcs.Repository.AuthorRepository;
import com.lms.bcs.Repository.GenreRepository;
import com.lms.bcs.Repository.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class BookMapper {
    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    public Book toEntity(BookDTO dto) {
        // Handle the Author mapping
        Author author = authorRepository.findById(dto.getAuthorId())
                .orElseThrow(() -> new EntityNotFoundException("Author not found"));

        Genre genre = genreRepository.getById(dto.getGenreId());

        Publisher publisher = publisherRepository.getById(dto.getPublisherId());

        Book book = Book.builder()
                .title(dto.getTitle())
                .ISBN(dto.getISBN())
                .publicationDate(dto.getPublicationDate())
                .rating(dto.getRating())
                .status(BookStatus.valueOf(dto.getStatus()))
                .summary(dto.getSummary())
                .coverImage(dto.getCoverImage())
                .author(author)
                .genre(genre)
                .publisher(publisher)
                .build();

        return book;
    }

    public BookDTO toDTO(Book book) {
        BookDTO dto = BookDTO.builder()
                .title(book.getTitle())
                .ISBN(book.getISBN())
                .authorId(book.getAuthor().getId())
                .genreId(book.getGenre().getId())
                .publisherId(book.getPublisher().getId())
                .publicationDate(book.getPublicationDate())
                .summary(book.getSummary())
                .coverImage(book.getCoverImage())
                .rating(book.getRating())
                .status(book.getStatus().toString())
                .build();
        return dto;
    }

}
