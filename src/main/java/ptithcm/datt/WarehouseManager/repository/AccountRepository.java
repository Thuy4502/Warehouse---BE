package ptithcm.datt.WarehouseManager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ptithcm.datt.WarehouseManager.model.Account;
import ptithcm.datt.WarehouseManager.model.Staff;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUsername(String username);
    Optional<Account> findByStaff(Staff staff);

}
