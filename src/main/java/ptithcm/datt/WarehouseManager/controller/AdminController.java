package ptithcm.datt.WarehouseManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ptithcm.datt.WarehouseManager.model.Staff;
import ptithcm.datt.WarehouseManager.model.Transaction;
import ptithcm.datt.WarehouseManager.response.EntityResponse;
import ptithcm.datt.WarehouseManager.service.StaffService;

import java.util.List;

@RestController
@RequestMapping("/v1/admin")
public class AdminController {
    @Autowired
    private StaffService staffService;

    @GetMapping("/getAll")
    public ResponseEntity<EntityResponse> getAllStaff() {
        List<Staff> staffList = staffService.getAllStaff();
        EntityResponse<List<Staff>> response = new EntityResponse<>();
        response.setData(staffList);
        response.setMessage("Get all staff request successfully");
        response.setCode(HttpStatus.OK.value());
        response.setStatus(HttpStatus.OK);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
