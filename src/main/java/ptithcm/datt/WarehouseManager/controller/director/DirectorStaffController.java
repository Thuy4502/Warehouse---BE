package ptithcm.datt.WarehouseManager.controller.director;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ptithcm.datt.WarehouseManager.model.Account;
import ptithcm.datt.WarehouseManager.model.Book;
import ptithcm.datt.WarehouseManager.model.Staff;
import ptithcm.datt.WarehouseManager.response.ApiResponse;
import ptithcm.datt.WarehouseManager.response.EntityResponse;
import ptithcm.datt.WarehouseManager.request.StaffRequest;
import ptithcm.datt.WarehouseManager.service.AccountService;
import ptithcm.datt.WarehouseManager.service.AuthService;
import ptithcm.datt.WarehouseManager.service.StaffService;

import java.util.List;

@RestController
@RequestMapping("v1/admin/staff")
public class DirectorStaffController {
    @Autowired
    private StaffService staffService;

    @Autowired
    AccountService accountService;

    @Autowired
    AuthService authService;


    @PutMapping("/changeStaffStatus/{staffId}")
    public ResponseEntity<EntityResponse> updateStaff(@PathVariable Long staffId, @RequestBody Staff staffRequest) {
        try {
            Staff updateStaff = staffService.changeStaffStatus(staffId, staffRequest);
            EntityResponse<Staff> response = new EntityResponse<>();
            response.setData(updateStaff);
            response.setMessage("Change staff status successfully");
            response.setCode(HttpStatus.OK.value());
            response.setStatus(HttpStatus.OK);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            EntityResponse<Book> response = new EntityResponse<>();
            response.setData(null);
            response.setMessage("Error change staff status: " + e.getMessage());
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setStatus(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/addStaff")
    public ResponseEntity<ApiResponse> addStaff(@RequestBody List<StaffRequest> staffRequests) {
        ApiResponse res = new ApiResponse();
        HttpStatus httpStatus = HttpStatus.CREATED;

        try {
            List<Account> savedAccounts = authService.addStaff(staffRequests);
            if (!savedAccounts.isEmpty()) {
                StringBuilder usernames = new StringBuilder();
                for (StaffRequest request : staffRequests) {
                    usernames.append(request.getUsername()).append(", ");
                }

                if (usernames.length() > 0) {
                    usernames.setLength(usernames.length() - 2);
                }

                res.setCode(HttpStatus.CREATED.value());
                res.setMessage("Staff created successfully for accounts: " + usernames.toString());
                res.setStatus(HttpStatus.CREATED);
            } else {
                res.setCode(HttpStatus.BAD_REQUEST.value());
                res.setMessage("No accounts were created.");
                res.setStatus(HttpStatus.BAD_REQUEST);
                httpStatus = HttpStatus.BAD_REQUEST;
            }
        } catch (Exception e) {
            System.out.println("error: " + e.getMessage());
            res.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            res.setMessage("An exception occurred on the server: " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(res, httpStatus);
    }








}