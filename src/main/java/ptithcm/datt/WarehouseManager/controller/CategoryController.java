package ptithcm.datt.WarehouseManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ptithcm.datt.WarehouseManager.model.Category;
import ptithcm.datt.WarehouseManager.request.CategoryRequest;
import ptithcm.datt.WarehouseManager.response.EntityResponse;
import ptithcm.datt.WarehouseManager.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/v1/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

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

    @PostMapping("/add")
    public ResponseEntity<EntityResponse> addCategory(@RequestBody Category category) {
        Category savedCategory = categoryService.createCategory(category);
        EntityResponse<Category> response = new EntityResponse<>();
        response.setData(savedCategory);
        response.setMessage("Create category successfully");
        response.setCode(HttpStatus.CREATED.value());
        response.setStatus(HttpStatus.CREATED);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/update/{categoryId}")
    public ResponseEntity<EntityResponse> updateCategory(@PathVariable("categoryId") Long categoryId, @RequestBody CategoryRequest category) {
        Category savedCategory = categoryService.updateCategory(categoryId, category);
        EntityResponse<Category> response = new EntityResponse<>();
        response.setData(savedCategory);
        response.setMessage("Create category successfully");
        response.setCode(HttpStatus.OK.value());
        response.setStatus(HttpStatus.OK);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
