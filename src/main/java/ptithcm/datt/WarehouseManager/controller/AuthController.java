package ptithcm.datt.WarehouseManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ptithcm.datt.WarehouseManager.dto.request.AccountRequest;
import ptithcm.datt.WarehouseManager.dto.request.AuthenticationRequest;
import ptithcm.datt.WarehouseManager.dto.request.IntrospectRequest;
import ptithcm.datt.WarehouseManager.dto.request.StaffRequest;
import ptithcm.datt.WarehouseManager.dto.response.AuthenticationResponse;
import ptithcm.datt.WarehouseManager.dto.response.IntrospectResponse;
import ptithcm.datt.WarehouseManager.model.Account;
import ptithcm.datt.WarehouseManager.response.ApiResponse;
import ptithcm.datt.WarehouseManager.response.EntityResponse;
import ptithcm.datt.WarehouseManager.service.AuthService;

import java.util.List;

@RestController
@RequestMapping("v1/auth")
public class AuthController {
    @Autowired
    AuthService authService;

    @Autowired
    AuthService accountService;
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerAccount(@RequestBody AccountRequest account) {
        ApiResponse res = new ApiResponse();
        HttpStatus httpStatus = null;
        try {
            Account savedAccount = accountService.createAccount(account);
            if(savedAccount.getAccountId() > 0) {
                res.setCode(HttpStatus.CREATED.value());
                res.setMessage("Customer is created successfully for account: " + account.getUsername());
                res.setStatus(HttpStatus.CREATED);
                httpStatus = HttpStatus.CREATED;
            }
        }
        catch (Exception e) {
            System.out.println("error" + e.getMessage());
            res.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            res.setMessage("An exception occured from server with exception: " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(res, httpStatus);

    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) throws Exception {
        AuthenticationResponse result = authService.authenticate(request);


        return ResponseEntity.ok(result);
    }

    @PostMapping("/introspect")
    public ResponseEntity<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request) throws Exception {
        IntrospectResponse result = authService.introspect(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/addStaff")
    public ResponseEntity<ApiResponse> addStaff(@RequestBody List<StaffRequest> staffRequests) {
        ApiResponse res = new ApiResponse();
        HttpStatus httpStatus = HttpStatus.CREATED;

        try {
            List<Account> savedAccounts = accountService.addStaff(staffRequests);
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
