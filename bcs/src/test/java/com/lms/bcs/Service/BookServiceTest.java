package com.lms.bcs.Service;

import com.lms.bcs.Entity.Book;
import com.lms.bcs.Entity.BookStatus;
import com.lms.bcs.Entity.Reservation;
import com.lms.bcs.Repository.BookRepository;
import com.lms.bcs.Repository.ReservationRepository;
import org.hibernate.service.spi.InjectService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;
    @Mock
    private ReservationRepository reservationRepository;

    @Test
    public void testReserveBookSuccess() {
        // Arrange
        Long bookId = 1L;
        Long userId = 101L;

        // Assume that the book with bookId is available in the database
        Book availablebook = new Book();
        availablebook.setId(bookId);
        availablebook.setStatus(BookStatus.AVAILABLE);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(availablebook));

        // Stub the behavior of reservationRepository.save
        when(reservationRepository.save(any(Reservation.class))).thenReturn(new Reservation());

        // Act
        boolean reservationResult = bookService.reserveBook(bookId, userId);

        // Assert
        assertTrue(reservationResult);

        // Check if a reservation entry is created
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    public void testReserveBookFailureBookNotAvailable() {
        // Arrange
        Long bookId = 2L;
        Long userId = 102L;

        // Assume that the book with bookId is not available in the database
        Book unavailableBook = new Book();
        unavailableBook.setId(bookId);
        unavailableBook.setStatus(BookStatus.BORROWED);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(unavailableBook));

        // Act
        boolean reservationResult = bookService.reserveBook(bookId, userId);

        // Assert
        assertFalse(reservationResult);
        assertEquals(BookStatus.BORROWED, unavailableBook.getStatus());

    }

    @Test
    public void testReserveBookFailureBookNotFound() {
        // Arrange
        Long nonExistentBookId = 3L;
        Long userId = 103L;

        when(bookRepository.findById(nonExistentBookId)).thenReturn(Optional.empty());

        // Act
        boolean reservationResult = bookService.reserveBook(nonExistentBookId, userId);

        // Assert
        assertFalse(reservationResult);

    }
    @Test
    public void testReserveBookBookAlreadyReserved() {
        Long bookId = 1L;
        Long userId = 2L;

        Book book = new Book();
        book.setId(bookId);
        book.setStatus(BookStatus.RESERVED);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        boolean result = bookService.reserveBook(bookId, userId);
        assertFalse(result);
        verify(bookRepository, Mockito.never()).save(Mockito.any(Book.class));

        // Verify that no reservation is created
        verify(reservationRepository, Mockito.never()).save(Mockito.any(Reservation.class));
    }
    @Test
    public void testReserveBookWithException() {
        // Arrange
        Long bookId = 1L;
        Long userId = 101L;

        // Assume that the book with bookId is available in the database
        Book availableBook = new Book();
        availableBook.setId(bookId);
        availableBook.setStatus(BookStatus.AVAILABLE);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(availableBook));

        // Stub the behavior of reservationRepository.save to throw an exception
        when(reservationRepository.save(any(Reservation.class))).thenThrow(new RuntimeException("Failed to save reservation"));

        // Act and Assert
        assertThrows(RuntimeException.class, () -> {
            bookService.reserveBook(bookId, userId);
        });

        // Check if a reservation entry is attempted to be created
        verify(reservationRepository, times(1)).save(any(Reservation.class));


    }
    @Test
    public void testReserveBookBookNotFound() {
        Long bookId = 1L;
        Long userId = 2L;


        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());


        boolean result = bookService.reserveBook(bookId, userId);
        assertFalse(result);
    }


    @Test
    public void testReserveBookBookNotAvailable() {
        Long bookId = 1L;
        Long userId = 2L;


        Book book = new Book();
        book.setId(bookId);
        book.setStatus(BookStatus.BORROWED);


        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        boolean result = bookService.reserveBook(bookId, userId);
        assertFalse(result);
    }
  /*  @Test
    public void testReserveBookInternalServerError() {
        // Arrange
        Long bookId = 1L;
        Long userId = 101L;

        // Assume that the book with bookId is available in the database
        Book availableBook = new Book();
        availableBook.setId(bookId);
        availableBook.setStatus(BookStatus.AVAILABLE);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(availableBook));

        // Simulate an error when saving reservation
        when(reservationRepository.save(any(Reservation.class))).thenThrow(new RuntimeException("Simulated internal server error"));

        // Act
        boolean reservationResult = bookService.reserveBook(bookId, userId);

        // Assert
        assertFalse(reservationResult); // Expecting the reservation to fail

        // Check if a reservation entry is attempted to be created
        Mockito.verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    public void testReserveBookInternalServerError1() {
        Long bookId = 2L;
        Long userId = 3L;

        // Stub the behavior of bookRepository.findById to throw a RuntimeException
        when(bookRepository.findById(bookId)).thenThrow(new RuntimeException("Simulated internal server error"));

        // Act
        boolean result = bookService.reserveBook(bookId, userId);

        // Assert
        assertFalse(result);

        // Verify that bookRepository.save is never called
        Mockito.verify(bookRepository, Mockito.never()).save(Mockito.any(Book.class));
    }
    @Test
    public void testReserveBook_emptybookId(){
        Long bookId = null;
        Long userId = 3L;

        // Assume that the book with bookId is available in the database
        Book availableBook = new Book();
        availableBook.setId(bookId);
        availableBook.setStatus(BookStatus.AVAILABLE);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(availableBook));
        when(bookRepository.findById(bookId)).thenReturn(null);

        boolean result = bookService.reserveBook(bookId, userId);
        assertFalse(result);
    }

    @Test
    public void testReserveBook_emptyUserId(){
        Long bookId = 2L;
        Long userId = null;
        when(bookRepository.findById(bookId)).thenReturn(null);

        Boolean result = bookService.reserveBook(bookId, userId);
        assertFalse(result);

    }*/
  @Test
  public void testSearchBookWithValidKeyword() {
      String keyword = "Spring";
      List<Book> expectedBooks = new ArrayList<>();
      // Add some sample books to expectedBooks list
      //expectedBooks.add(new Book("123456789", "Java Programming", "John Doe"));
      //expectedBooks.add(new Book("987654321", "Advanced Java", "Jane Smith"));

      when(bookRepository.search(keyword)).thenReturn(expectedBooks);


      List<Book> actualBooks = bookService.searchBook(keyword);

      assertEquals(expectedBooks, actualBooks);

      verify(bookRepository,times(1)).search(keyword);
  }
    @Test
    public void testSearchBookWithEmptyKeyword() {
        String keyword = "";
        List<Book> expectedBooks = new ArrayList<>();
        // Adding sample books to the expectedBooks list
        //expectedBooks.add(new Book("123456789", "Java Programming", "John Doe"));
        //expectedBooks.add(new Book("987654321", "Advanced Java", "Jane Smith"));

        when(bookRepository.findAll()).thenReturn(expectedBooks);

        List<Book> actualBooks = bookService.searchBook(keyword);

        assertEquals(expectedBooks, actualBooks);
    }
    @Test
    public void testSearchBookWithInvalidKeyword() {
        String keyword = "InvalidKeyword";
        List<Book> expectedBooks = new ArrayList<>();

        // Stubbing the behavior when the keyword does not match any books
        when(bookRepository.search(keyword)).thenReturn(expectedBooks);

        List<Book> actualBooks = bookService.searchBook(keyword);

        // Assert that the actual books list is empty
        assertTrue(actualBooks.isEmpty());
    }

    @Test
    public void testSearchBookWithValidKeywordAndException() {
        String keyword = "Spring";
        String expectedErrorMessage = "Database connection error";

        // Stubbing the behavior to throw an exception when the search method is called
        when(bookRepository.search(keyword)).thenThrow(new RuntimeException(expectedErrorMessage));

        // Assert that the method call throws an exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            bookService.searchBook(keyword);
        });

        // Assert that the exception message matches the expected error message
        assertEquals(expectedErrorMessage, exception.getMessage());
    }
}
