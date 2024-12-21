package ptithcm.datt.WarehouseManager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptithcm.datt.WarehouseManager.model.Book;
import ptithcm.datt.WarehouseManager.model.CategoryBook;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryBookRepository extends JpaRepository<CategoryBook, Long> {
    List<CategoryBook> findByBook(Book book);
}
