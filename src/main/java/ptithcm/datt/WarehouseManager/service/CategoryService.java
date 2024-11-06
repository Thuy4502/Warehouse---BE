package ptithcm.datt.WarehouseManager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ptithcm.datt.WarehouseManager.model.Category;
import ptithcm.datt.WarehouseManager.model.Staff;
import ptithcm.datt.WarehouseManager.repository.CategoryRepository;
import ptithcm.datt.WarehouseManager.repository.StaffRepository;
import ptithcm.datt.WarehouseManager.request.CategoryRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private StaffRepository staffRepository;

    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long categoryId, CategoryRequest categoryReq) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);

        if(optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            category.setCategoryName(categoryReq.getCategoryName());
            category.setDescription(categoryReq.getDescription());
            category.setUpdateAt(LocalDateTime.now());
            Staff staff = staffRepository.findById(categoryReq.getStaffId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            category.setStaff(staff);
            return categoryRepository.save(category);
        } else {
            throw new RuntimeException("Category not found with ID: " + categoryId);
        }

    }

}
