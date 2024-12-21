package ptithcm.datt.WarehouseManager.controller.warehousekeeper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ptithcm.datt.WarehouseManager.model.Book;
import ptithcm.datt.WarehouseManager.response.EntityResponse;
import ptithcm.datt.WarehouseManager.request.BookExcelRequest;
import ptithcm.datt.WarehouseManager.request.BookRequest;
import ptithcm.datt.WarehouseManager.service.BookService;

import java.util.List;

@RestController
@RequestMapping("v1/warehouse_keeper/book")
public class WHKBookController {
    @Autowired
    private BookService bookService;

    @PostMapping("/add")
    public ResponseEntity<EntityResponse<Book>> addBook(@RequestBody BookRequest bookRequest) {

        EntityResponse<Book> response = new EntityResponse<>();

        try {
            Book createdBook = bookService.createBook(bookRequest);
            response.setData(createdBook);
            response.setMessage("Book created successfully");
            response.setCode(HttpStatus.CREATED.value());
            response.setStatus(HttpStatus.CREATED);
            return new ResponseEntity<>(response, response.getStatus());
        } catch (Exception e) {
            response.setData(null);
            response.setMessage("Error creating book: " + e.getMessage());
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setStatus(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(response, response.getStatus());
        }
    }

    @PostMapping("/addByExcel")
    public ResponseEntity<EntityResponse<List<Book>>> addBooksByExcel(@RequestBody List<BookExcelRequest> bookRequest) {

        EntityResponse<List<Book>> response = new EntityResponse<>();
        try {
            List<Book> createdBook = bookService.createBooks(bookRequest);
            response.setData(createdBook);
            response.setMessage("Books created by excel file successfully");
            response.setCode(HttpStatus.CREATED.value());
            response.setStatus(HttpStatus.CREATED);
            return new ResponseEntity<>(response, response.getStatus());
        } catch (Exception e) {
            response.setData(null);
            response.setMessage("Error creating book: " + e.getMessage());
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setStatus(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(response,response.getStatus());
        }
    }

    @PutMapping("/update/{bookId}")
    public ResponseEntity<EntityResponse<Book>> updateBook(@PathVariable Long bookId, @RequestBody BookRequest bookRequest) {

        EntityResponse<Book> response = new EntityResponse<>();

        try {
            Book updateBook = bookService.updateBook(bookId, bookRequest);
            response.setData(updateBook);
            response.setMessage("Book updated successfully");
            response.setCode(HttpStatus.OK.value());
            response.setStatus(HttpStatus.OK);
            return new ResponseEntity<>(response, response.getStatus());
        } catch (Exception e) {
            response.setData(null);
            response.setMessage("Error updating book: " + e.getMessage());
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setStatus(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(response, response.getStatus());
        }
    }
}
