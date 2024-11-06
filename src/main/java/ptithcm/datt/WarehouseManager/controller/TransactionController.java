package ptithcm.datt.WarehouseManager.controller;

import jakarta.persistence.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ptithcm.datt.WarehouseManager.dto.response.TransactionHistoryResponse;
import ptithcm.datt.WarehouseManager.model.Transaction;
import ptithcm.datt.WarehouseManager.model.TransactionRequest;
import ptithcm.datt.WarehouseManager.request.TransactionReq;
import ptithcm.datt.WarehouseManager.response.EntityResponse;
import ptithcm.datt.WarehouseManager.service.TransactionService;

import java.util.List;

@RestController
@RequestMapping("/v1/transaction")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping("/add")
    public ResponseEntity<EntityResponse> addTransaction(@RequestBody TransactionReq transactionReq) {
        try {
            Transaction result = transactionService.createTransaction(transactionReq);
            EntityResponse<Transaction> response = new EntityResponse<>();
            response.setData(result);
            response.setMessage("Transaction created successfully");
            response.setCode(HttpStatus.CREATED.value());
            response.setStatus(HttpStatus.CREATED);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            EntityResponse<TransactionRequest> response = new EntityResponse<>();
            response.setData(null);
            response.setMessage("Error creating transaction: " + e.getMessage());
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setStatus(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update/{transactionId}")
    public ResponseEntity<EntityResponse> updateTransaction(@PathVariable Long transactionId, @RequestBody TransactionReq transactionReq) {
        try {
            Transaction result = transactionService.updateTransaction(transactionId, transactionReq);
            EntityResponse<Transaction> response = new EntityResponse<>();
            response.setData(result);
            response.setMessage("Transaction updated successfully");
            response.setCode(HttpStatus.CREATED.value());
            response.setStatus(HttpStatus.CREATED);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            EntityResponse<TransactionRequest> response = new EntityResponse<>();
            response.setData(null);
            response.setMessage("Error updating transaction: " + e.getMessage());
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setStatus(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAll/{type}")
    public ResponseEntity<EntityResponse> getAllTransaction(@PathVariable String type)
    {
        List<Transaction> transactionList = transactionService.getAllTransaction(type);
        EntityResponse<List<Transaction>> response = new EntityResponse<>();
        response.setData(transactionList);
        response.setMessage("Get all transaction request successfully");
        response.setCode(HttpStatus.OK.value());
        response.setStatus(HttpStatus.OK);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/history")
    public ResponseEntity<EntityResponse> getTransactionHistory(@RequestParam(value = "type", required = false) String type) {
        List<TransactionHistoryResponse> historyResponses = transactionService.getTransactionHistory(type);
        EntityResponse<List<TransactionHistoryResponse>> response = new EntityResponse<>();
        response.setData(historyResponses);
        response.setMessage("Get history transactions request successfully");
        response.setCode(HttpStatus.OK.value());
        response.setStatus(HttpStatus.OK);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
