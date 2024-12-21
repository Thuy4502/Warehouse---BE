package ptithcm.datt.WarehouseManager.controller.stockdepartment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ptithcm.datt.WarehouseManager.model.TransactionRequest;
import ptithcm.datt.WarehouseManager.request.TransactionRequestDTO;
import ptithcm.datt.WarehouseManager.response.EntityResponse;
import ptithcm.datt.WarehouseManager.service.TransactionRequestService;

@RestController
@RequestMapping("/v1/stockdepartment/transaction_request")
public class SDTransactionRequestController {
    @Autowired
    private TransactionRequestService transactionRequestService;

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
