package ptithcm.datt.WarehouseManager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ptithcm.datt.WarehouseManager.model.Staff;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {

    @Query("SELECT s FROM Staff s WHERE s.staffId = (SELECT a.staff.staffId FROM Account a WHERE a.accountId = :accountId)")
    Staff findStaffByUserId(@Param("accountId") Long accountId);

}

