package ptithcm.datt.WarehouseManager.controller.warehousekeeper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ptithcm.datt.WarehouseManager.model.Author;
import ptithcm.datt.WarehouseManager.response.EntityResponse;
import ptithcm.datt.WarehouseManager.service.AuthorService;

@RestController
@RequestMapping("v1/warehouse_keeper/author")
public class WHKAuthorController {
    @Autowired
    AuthorService authorService;

    @PostMapping("/add")
    public ResponseEntity<EntityResponse<Author>> addAuthor(@RequestBody Author author) {
        Author savedAuthor = authorService.addAuthor(author);
        EntityResponse<Author> response = new EntityResponse<>();
        response.setData(savedAuthor);
        response.setMessage("Author added successfully");
        response.setCode(HttpStatus.CREATED.value());
        response.setStatus(HttpStatus.CREATED);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/update/{authorId}")
    public ResponseEntity<EntityResponse> updateAuthor(
            @PathVariable Long authorId,
            @RequestBody Author authorRequest) {
        try {
            Author updatedAuthor = authorService.updateAuthor(authorId, authorRequest);

            EntityResponse<Author> response = new EntityResponse<>();
            response.setData(updatedAuthor);
            response.setMessage("Author updated successfully");
            response.setCode(HttpStatus.OK.value());
            response.setStatus(HttpStatus.OK);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            EntityResponse<Author> response = new EntityResponse<>();
            response.setData(null);
            response.setMessage("Error updating author: " + e.getMessage());
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setStatus(HttpStatus.BAD_REQUEST);

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
