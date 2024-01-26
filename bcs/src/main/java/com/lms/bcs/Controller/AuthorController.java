package com.lms.bcs.Controller;

import com.lms.bcs.Dto.AuthorDTO;
import com.lms.bcs.Entity.Author;
import com.lms.bcs.Service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {
    @Autowired
    private AuthorService authorService;

    @PostMapping
    public Author createAuthor(@RequestBody AuthorDTO author){
        return authorService.saveAuthor(author);

    }
    @GetMapping
    public List<Author> getAllAuthors(){
         return authorService.findAllAuthors();
    }
    @GetMapping("/{id}")
    public Author getAuthorById(@PathVariable Long id) {
        return authorService.findAuthorById(id);
    }



}
