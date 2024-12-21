package ptithcm.datt.WarehouseManager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ptithcm.datt.WarehouseManager.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByCategoryName(String name);
    List<Category> findByCategoryNameIn(List<String> categoryNames);

}
