package ptithcm.datt.WarehouseManager.controller.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ptithcm.datt.WarehouseManager.model.Transaction;
import ptithcm.datt.WarehouseManager.response.EntityResponse;
import ptithcm.datt.WarehouseManager.response.TransactionHistoryResponse;
import ptithcm.datt.WarehouseManager.service.TransactionService;

import java.util.List;

@RestController
@RequestMapping("/v1/api/transaction")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

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
        return new ResponseEntity<>(response, response.getStatus());
    }

}
