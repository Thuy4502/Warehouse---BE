package ptithcm.datt.WarehouseManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ptithcm.datt.WarehouseManager.model.Book;
import ptithcm.datt.WarehouseManager.model.TransactionRequest;
import ptithcm.datt.WarehouseManager.request.BookRequest;
import ptithcm.datt.WarehouseManager.request.TransactionRequestDTO;
import ptithcm.datt.WarehouseManager.response.EntityResponse;
import ptithcm.datt.WarehouseManager.service.TransactionRequestService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/transaction_request")
public class TransactionRequestController {

    @Autowired
    private TransactionRequestService transactionRequestService;

    @GetMapping("/getAll/{type}")
    public ResponseEntity<EntityResponse> getAllTransactionRequest(@PathVariable String type) {
        List<TransactionRequest> transactionRequestList = transactionRequestService.getAllTransactionRequestByType(type);
        EntityResponse<List<TransactionRequest>> response = new EntityResponse<>();
        response.setData(transactionRequestList);
        response.setMessage("Get all transaction request successfully");
        response.setCode(HttpStatus.OK.value());
        response.setStatus(HttpStatus.OK);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<EntityResponse> addTransactionRequest(@RequestBody TransactionRequestDTO transactionRequestDTO) {

        try {
            TransactionRequest result = transactionRequestService.createTransactionRequest(transactionRequestDTO);
            EntityResponse<TransactionRequest> response = new EntityResponse<>();
            response.setData(result);
            response.setMessage("Transaction request created successfully");
            response.setCode(HttpStatus.CREATED.value());
            response.setStatus(HttpStatus.CREATED);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            EntityResponse<TransactionRequest> response = new EntityResponse<>();
            response.setData(null);
            response.setMessage("Error creating transaction request: " + e.getMessage());
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setStatus(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<EntityResponse> updateBook(@PathVariable Long id, @RequestBody TransactionRequestDTO transactionRequestDTO) {
        try {
            TransactionRequest result = transactionRequestService.updateTransactionRequest(id, transactionRequestDTO);
            EntityResponse<TransactionRequest> response = new EntityResponse<>();
            response.setData(result);
            response.setMessage("Transaction request created successfully");
            response.setCode(HttpStatus.OK.value());
            response.setStatus(HttpStatus.OK);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            EntityResponse<TransactionRequest> response = new EntityResponse<>();
            response.setData(null);
            response.setMessage("Error creating book: " + e.getMessage());
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setStatus(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
