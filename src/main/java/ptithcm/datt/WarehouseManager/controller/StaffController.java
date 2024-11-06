package ptithcm.datt.WarehouseManager.controller;

import jdk.jshell.spi.ExecutionControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ptithcm.datt.WarehouseManager.model.Account;
import ptithcm.datt.WarehouseManager.model.Book;
import ptithcm.datt.WarehouseManager.model.Staff;
import ptithcm.datt.WarehouseManager.request.BookRequest;
import ptithcm.datt.WarehouseManager.request.ChangePasswordRequest;
import ptithcm.datt.WarehouseManager.response.ApiResponse;
import ptithcm.datt.WarehouseManager.response.EntityResponse;
import ptithcm.datt.WarehouseManager.service.AccountService;
import ptithcm.datt.WarehouseManager.service.StaffService;

import java.util.List;

@RestController
@RequestMapping("v1/staff")
public class StaffController {
    @Autowired
    private StaffService staffService;

    @Autowired
    AccountService accountService;

    @GetMapping("/profile")
    public ResponseEntity<EntityResponse> getProfile(@RequestHeader("Authorization") String jwt) {
        EntityResponse res = new EntityResponse<>();
        System.out.println("Tokennnnnnnnnnnnnnnnnnnnnnnn" + jwt);
        if (jwt == null) {
            throw new RuntimeException("JWT token is missing");
        }
        Account account = accountService.findUserProfileByJwt(jwt);
        Staff staff = staffService.findStaffByAccountId(account.getAccountId());
        res.setData(staff);
        res.setCode(HttpStatus.OK.value());
        res.setStatus(HttpStatus.OK);
        res.setMessage("success get customer");
        return new ResponseEntity<>(res,res.getStatus());
    }

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

    @PutMapping("/update/{staffId}")
    public ResponseEntity<EntityResponse> updateStaff(@PathVariable Long staffId, @RequestBody Staff staffRequest) {
        try {
            Staff updateStaff = staffService.updateStaff(staffId, staffRequest);
            EntityResponse<Staff> response = new EntityResponse<>();
            response.setData(updateStaff);
            response.setMessage("Staff updated successfully");
            response.setCode(HttpStatus.OK.value());
            response.setStatus(HttpStatus.OK);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            EntityResponse<Book> response = new EntityResponse<>();
            response.setData(null);
            response.setMessage("Error updating staff: " + e.getMessage());
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setStatus(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/change/password")
    public ResponseEntity<ApiResponse> changePasswordByJwt(@RequestHeader("Authorization") String jwt, @RequestBody ChangePasswordRequest rq){
        ApiResponse res = new ApiResponse();
        try{
            Account update = accountService.changePassword(jwt,rq);
            res.setCode(HttpStatus.OK.value());
            res.setStatus(HttpStatus.OK);
            res.setMessage("success");
        }catch (Exception e){
            res.setCode(HttpStatus.CONFLICT.value());
            res.setStatus(HttpStatus.CONFLICT);
            res.setMessage("error " + e.getMessage());
        }
        return new ResponseEntity<>(res, res.getStatus());
    }








}
