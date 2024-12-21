package ptithcm.datt.WarehouseManager.controller.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ptithcm.datt.WarehouseManager.model.Category;
import ptithcm.datt.WarehouseManager.response.EntityResponse;
import ptithcm.datt.WarehouseManager.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("v1/api/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping("/getAll")
    public ResponseEntity<EntityResponse> getAllCategory() {
        List<Category> categoryList = categoryService.getAllCategory();
        EntityResponse<List<Category>> response = new EntityResponse<>();
        response.setData(categoryList);
        response.setMessage("Get all category successfully");
        response.setCode(HttpStatus.OK.value());
        response.setStatus(HttpStatus.OK);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
