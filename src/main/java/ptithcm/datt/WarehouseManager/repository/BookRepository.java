package ptithcm.datt.WarehouseManager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptithcm.datt.WarehouseManager.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}
