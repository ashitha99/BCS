package com.lms.bcs.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lms.bcs.Dto.BookDTO;
import com.lms.bcs.Entity.Book;
import com.lms.bcs.Service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
public class BookControllerTest {


    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookService bookService;

    @MockBean
    private BookController bookController;

    private Book book;
    private BookDTO bookDTO;


    @BeforeEach
    public void setup() {
        bookDTO = BookDTO.builder()
                .authorId(1L)
                .publicationDate(new Date(2005-07-16))
                .summary("a test book")
                .coverImage("a cover image")
                .ISBN("1234567")
                .genreId(1L)
                .publisherId(1L)
                .rating(5.0)
                .status("AVAILABLE")
                .title("Sample Book")
                .build();

        book = new Book(); // set up your entity here
    }

    @Test
    public void testCreateBook() throws Exception {
        when(bookService.saveBook(bookDTO)).thenReturn(bookDTO);

        ObjectMapper objectMapper = new ObjectMapper();
        String bookJson = objectMapper.writeValueAsString(bookDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAllBooks() throws Exception {
        List<Book> books = Arrays.asList(book);
        when(bookService.findAllBooks()).thenReturn(books);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").exists()); // add more detailed assertions if needed
    }

    @Test
    public void testGetBookById() throws Exception {
        Long id = 1L;
        when(bookService.findBookById(id)).thenReturn(book);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/books/" + id))
                .andExpect(status().isOk());
        // add more detailed assertions based on the expected response
    }


    @Test
    public void testReserveBookSuccess() {
        // Arrange
        Long bookId = 1L;
        Long userId = 101L;
        String validToken = "Bearer validToken";
        when(bookService.validateJwtToken("validToken")).thenReturn(true);
        when(bookService.reserveBook(bookId, userId)).thenReturn(true);

        // Act
        ResponseEntity<String> response = bookController.reserveBook(bookId, userId, validToken);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    @Test
    public void testReserveBookInvalidToken() {
        // Arrange
        Long bookId = 1L;
        Long userId = 101L;
        String invalidToken = "Bearer invalidToken";
        when(bookService.validateJwtToken("invalidToken")).thenReturn(false);

        // Act
        ResponseEntity<String> response = bookController.reserveBook(bookId, userId, invalidToken);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid token", response.getBody());
    }
    @Test
    public void testReserveBookFailure() {
        // Arrange
        Long bookId = 1L;
        Long userId = 101L;
        String validToken = "Bearer validToken";
        when(bookService.validateJwtToken("validToken")).thenReturn(true);
        when(bookService.reserveBook(bookId, userId)).thenReturn(false);

        // Act
        ResponseEntity<String> response = bookController.reserveBook(bookId, userId, validToken);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to reserve a book", response.getBody());
    }


}
