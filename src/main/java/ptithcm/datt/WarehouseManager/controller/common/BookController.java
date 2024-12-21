package ptithcm.datt.WarehouseManager.controller.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ptithcm.datt.WarehouseManager.exception.BookException;
import ptithcm.datt.WarehouseManager.model.Book;
import ptithcm.datt.WarehouseManager.response.EntityResponse;
import ptithcm.datt.WarehouseManager.service.BookService;

import java.util.List;

@RestController
@RequestMapping("v1/api/book")
public class BookController {
    @Autowired
    BookService bookService;

    @GetMapping("/getAll")
    public ResponseEntity<EntityResponse> getAllBook() {
        List<Book> bookList = bookService.getAllBook();
        EntityResponse<List<Book>> response = new EntityResponse<>();
        response.setData(bookList);
        response.setMessage("Get all book successfully");
        response.setCode(HttpStatus.OK.value());
        response.setStatus(HttpStatus.OK);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping("/id/{bookId}")
    public ResponseEntity<EntityResponse> findBookById(@PathVariable Long bookId) throws BookException {
        Book book = bookService.findBookById(bookId);
        EntityResponse<Book> response = new EntityResponse<>();
        response.setData(book);
        response.setMessage("Find book by id successfully");
        response.setCode(HttpStatus.OK.value());
        response.setStatus(HttpStatus.OK);
        return new ResponseEntity<>(response, response.getStatus());
    }
}
