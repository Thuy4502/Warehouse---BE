package ptithcm.datt.WarehouseManager.controller.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ptithcm.datt.WarehouseManager.model.Author;
import ptithcm.datt.WarehouseManager.response.EntityResponse;
import ptithcm.datt.WarehouseManager.service.AuthorService;

import java.util.List;

@RestController
@RequestMapping("/v1/api/author")
public class AuthorController {
    @Autowired
    private AuthorService authorService;

    @GetMapping("/getAll")
    public ResponseEntity<EntityResponse> getAllAuthor() {
        List<Author> authorList = authorService.getAllAuthor();
        EntityResponse<List<Author>> response = new EntityResponse<>();
        response.setData(authorList);
        response.setMessage("Get all author successfully");
        response.setCode(HttpStatus.OK.value());
        response.setStatus(HttpStatus.OK);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
