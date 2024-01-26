package com.lms.bcs.Controller;

import com.lms.bcs.Dto.BookDTO;
import com.lms.bcs.Entity.Book;
import com.lms.bcs.Service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    @Autowired
    private BookService bookService;

    @PostMapping
    public BookDTO createBook(@RequestBody BookDTO book) {
        return bookService.saveBook(book);
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.findAllBooks();
    }

    @GetMapping("/{id}")
    public Book getBookById(@PathVariable Long id) {
        return bookService.findBookById(id);
    }

    @GetMapping("/books")
    public List<Book> searchBook(@RequestParam(required = false) String keyword) {
        return bookService.searchBook(keyword);
    }

    @PostMapping("/catalog/reserve/{bookId}")
    public ResponseEntity<String> reserveBook(@PathVariable Long bookId, Long userId,@RequestHeader("Authorization") String bearerToken){
        //JWT token validation logic
        //Authorization-"Bearer fgrtjujtwhytjuikyhyjukike46";
        //Extract the actual token from bearer string

        String jwtToken=bearerToken.substring(7);
        //Validate the JWT token
       if(! bookService.validateJwtToken(jwtToken)){
           return new ResponseEntity<>("Invalid token",HttpStatus.UNAUTHORIZED);
       }
        //Book reservation logic.
        if(!bookService.reserveBook(bookId,userId)){
            return new ResponseEntity<>("Failed to reserve a book",HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("Book reserved successfully", HttpStatus.OK);
    }


}
