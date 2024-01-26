package com.lms.bcs.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.lms.bcs.Dto.BookDTO;
import com.lms.bcs.Entity.Book;
import com.lms.bcs.Entity.BookStatus;
import com.lms.bcs.Entity.Reservation;
import com.lms.bcs.Repository.BookRepository;
import com.lms.bcs.Repository.ReservationRepository;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
public class BookService {
    private static final Logger logger = LoggerFactory.getLogger(BookService.class);
    private static final String SECRET="ourSecretKey";

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private BookMapper mapper;

    public BookDTO saveBook(BookDTO book) {
        logger.info("book: {} is being saved to the database",book.getTitle());
        Book bookDetails = mapper.toEntity(book);
        Book savedBook = bookRepository.save(bookDetails);
        return mapper.toDTO(savedBook);
    }

    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    public Book findBookById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    public List<Book> findBooksByGenre(Long genreId) {
        // Implement query to find books by genre
        return null;
    }

    public List<Book> findBooksByAuthor(Long authorId) {
        // Implement query to find books by author
        return null;
    }

    public List<Book> findBooksByRating(Double rating) {
        // Implement query to find books by rating
        return null;
    }
    public List<Book> searchBook(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return bookRepository.findAll();
        }
        return bookRepository.search(keyword);
    }

    public boolean validateJwtToken(String jwtToken) {
        //validate the JWT token
        //this can include  decoding the JWT and checking the issuer, expiration etc.

        //Add the dependency for Autho
        //Create a secret key
        try{
            //using HMAC256 algorithm to verify the token
            Algorithm algorithm=Algorithm.HMAC256(SECRET);

            //verify the token
            DecodedJWT jwt= JWT.require(algorithm)
                    .build()
                    .verify(jwtToken);
            //check the expiry
            Date expiresAt=jwt.getExpiresAt();
            if(expiresAt.before(new Date())){
                return false;
            }
            //check the issuer
            Claim issuerClaim=jwt.getClaim("role");
            if(issuerClaim==null){
                return false;
            }
            return true;

        }catch (JWTVerificationException e){
            //invalid token
            return false;
        }
    }

    public boolean reserveBook(Long bookId,Long userId) {
        //find if the book is available by querying the DB
        //update the status reserved
        //tag the userId with reservation

        Book book=bookRepository.findById((bookId)).orElse(null);
        if(book!=null && BookStatus.AVAILABLE.equals(book.getStatus())){
            book.setStatus(BookStatus.RESERVED);
            bookRepository.save(book);

            //create a new reservation entry
            Reservation reservation=new Reservation();
            reservation.setBookId(bookId);
            reservation.setUserid(userId);
            reservation.setReservationDate(new Date());
            reservationRepository.save(reservation);
            return true;
        }
        return false;
    }
}
