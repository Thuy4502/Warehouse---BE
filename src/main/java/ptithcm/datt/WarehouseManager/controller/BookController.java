package ptithcm.datt.WarehouseManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ptithcm.datt.WarehouseManager.exception.BookException;
import ptithcm.datt.WarehouseManager.model.Book;
import ptithcm.datt.WarehouseManager.model.Category;
import ptithcm.datt.WarehouseManager.request.BookExcelRequest;
import ptithcm.datt.WarehouseManager.request.BookRequest;
import ptithcm.datt.WarehouseManager.response.EntityResponse;
import ptithcm.datt.WarehouseManager.service.BookService;

import java.util.List;

@RestController
@RequestMapping("/v1/book")
public class BookController {
    @Autowired
    private BookService bookService;
    @PostMapping("/add")
    public ResponseEntity<EntityResponse> addBook(@RequestBody BookRequest bookRequest) {
        try {
            Book createdBook = bookService.createBook(bookRequest);
            EntityResponse<Book> response = new EntityResponse<>();
            response.setData(createdBook);
            response.setMessage("Book created successfully");
            response.setCode(HttpStatus.CREATED.value());
            response.setStatus(HttpStatus.CREATED);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            EntityResponse<Book> response = new EntityResponse<>();
            response.setData(null);
            response.setMessage("Error creating book: " + e.getMessage());
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setStatus(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/addByExcel")
    public ResponseEntity<EntityResponse> addBooksByExcel(@RequestBody List<BookExcelRequest> bookRequest) {
        try {
            List<Book> createdBook = bookService.createBooks(bookRequest);
            EntityResponse<List<Book>> response = new EntityResponse<>();
            response.setData(createdBook);
            response.setMessage("Books created by excel file successfully");
            response.setCode(HttpStatus.CREATED.value());
            response.setStatus(HttpStatus.CREATED);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            EntityResponse<Book> response = new EntityResponse<>();
            response.setData(null);
            response.setMessage("Error creating book: " + e.getMessage());
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setStatus(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<EntityResponse> getAllBook() {
        List<Book> bookList = bookService.getAllBook();
        EntityResponse<List<Book>> response = new EntityResponse<>();
        response.setData(bookList);
        response.setMessage("Get all book successfully");
        response.setCode(HttpStatus.OK.value());
        response.setStatus(HttpStatus.OK);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/id/{bookId}")
    public ResponseEntity<EntityResponse> findBookById(@PathVariable Long bookId) throws BookException {
        Book book = bookService.findBookById(bookId);
        EntityResponse<Book> response = new EntityResponse<>();
        response.setData(book);
        response.setMessage("Find book by id successfully");
        response.setCode(HttpStatus.OK.value());
        response.setStatus(HttpStatus.OK);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/update/{bookId}")
    public ResponseEntity<EntityResponse> updateBook(@PathVariable Long bookId, @RequestBody BookRequest bookRequest) {
        try {
            Book updateBook = bookService.updateBook(bookId, bookRequest);
            EntityResponse<Book> response = new EntityResponse<>();
            response.setData(updateBook);
            response.setMessage("Book updated successfully");
            response.setCode(HttpStatus.OK.value());
            response.setStatus(HttpStatus.OK);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            EntityResponse<Book> response = new EntityResponse<>();
            response.setData(null);
            response.setMessage("Error updating book: " + e.getMessage());
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setStatus(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }


}
