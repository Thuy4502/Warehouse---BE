package ptithcm.datt.WarehouseManager.controller.warehousekeeper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ptithcm.datt.WarehouseManager.model.Category;
import ptithcm.datt.WarehouseManager.response.EntityResponse;
import ptithcm.datt.WarehouseManager.request.CategoryRequest;
import ptithcm.datt.WarehouseManager.service.CategoryService;

@RestController
@RequestMapping("/v1/warehouse_keeper/category")
public class WHKCategoryController {
    @Autowired
    private CategoryService categoryService;

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
