package ptithcm.datt.WarehouseManager.controller.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ptithcm.datt.WarehouseManager.model.TransactionRequest;
import ptithcm.datt.WarehouseManager.response.EntityResponse;
import ptithcm.datt.WarehouseManager.service.TransactionRequestService;

import java.util.List;

@RestController
@RequestMapping("/v1/api/transaction_request")
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
}
