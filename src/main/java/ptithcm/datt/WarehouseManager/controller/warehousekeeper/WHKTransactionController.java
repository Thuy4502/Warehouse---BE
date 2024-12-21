package ptithcm.datt.WarehouseManager.controller.warehousekeeper;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ptithcm.datt.WarehouseManager.model.Transaction;
import ptithcm.datt.WarehouseManager.model.TransactionRequest;
import ptithcm.datt.WarehouseManager.response.EntityResponse;
import ptithcm.datt.WarehouseManager.request.TransactionReq;
import ptithcm.datt.WarehouseManager.service.TransactionService;

@RestController
@RequestMapping("/v1/warehouse_keeper/transaction")
public class WHKTransactionController {
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
        Logger logger = LoggerFactory.getLogger(getClass()); // Tạo logger

        try {
            Transaction result = transactionService.updateTransaction(transactionId, transactionReq);
            EntityResponse<Transaction> response = new EntityResponse<>();
            response.setData(result);
            response.setMessage("Transaction updated successfully");
            response.setCode(HttpStatus.CREATED.value());
            response.setStatus(HttpStatus.CREATED);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            logger.error("EntityNotFoundException: " + e.getMessage(), e); // Ghi log chi tiết lỗi EntityNotFoundException
            EntityResponse<TransactionRequest> response = new EntityResponse<>();
            response.setData(null);
            response.setMessage("Error updating transaction: " + e.getMessage());
            response.setCode(HttpStatus.NOT_FOUND.value());
            response.setStatus(HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            logger.error("RuntimeException: " + e.getMessage(), e); // Ghi log chi tiết lỗi RuntimeException
            EntityResponse<TransactionRequest> response = new EntityResponse<>();
            response.setData(null);
            response.setMessage("Error updating transaction: " + e.getMessage());
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setStatus(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage(), e); // Ghi log chi tiết lỗi chung
            EntityResponse<TransactionRequest> response = new EntityResponse<>();
            response.setData(null);
            response.setMessage("Unexpected error: " + e.getMessage());
            response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
