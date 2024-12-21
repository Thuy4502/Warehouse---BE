package ptithcm.datt.WarehouseManager.controller.stockdepartment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ptithcm.datt.WarehouseManager.model.Account;
import ptithcm.datt.WarehouseManager.model.Book;
import ptithcm.datt.WarehouseManager.model.Staff;
import ptithcm.datt.WarehouseManager.request.StaffRequest;
import ptithcm.datt.WarehouseManager.response.ApiResponse;
import ptithcm.datt.WarehouseManager.response.EntityResponse;
import ptithcm.datt.WarehouseManager.service.AccountService;
import ptithcm.datt.WarehouseManager.service.AuthService;
import ptithcm.datt.WarehouseManager.service.StaffService;

import java.util.List;

@RestController
@RequestMapping("v1/stockdepartment/staff")
public class StaffController {
    @Autowired
    private StaffService staffService;

    @Autowired
    AccountService accountService;

    @Autowired
    AuthService authService;

    @GetMapping("/getAll")
    public ResponseEntity<EntityResponse> getAllStaff(@RequestHeader("Authorization") String jwt) {
        EntityResponse res = new EntityResponse<>();
        List<Staff> staff = staffService.getAllStaff();
        res.setData(staff);
        res.setCode(HttpStatus.OK.value());
        res.setStatus(HttpStatus.OK);
        res.setMessage("Get all staff successfully");
        return new ResponseEntity<>(res,res.getStatus());
    }




}