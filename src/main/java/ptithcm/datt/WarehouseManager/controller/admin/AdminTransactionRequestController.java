package ptithcm.datt.WarehouseManager.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ptithcm.datt.WarehouseManager.model.TransactionRequest;
import ptithcm.datt.WarehouseManager.response.EntityResponse;
import ptithcm.datt.WarehouseManager.request.TransactionRequestDTO;
import ptithcm.datt.WarehouseManager.service.TransactionRequestService;

@RestController
@RequestMapping("/v1/admin/transaction_request")
public class AdminTransactionRequestController {

    @Autowired
    private TransactionRequestService transactionRequestService;

    @PutMapping("/update/{id}")
    public ResponseEntity<EntityResponse> updateTransactionRequest(@PathVariable Long id, @RequestBody TransactionRequestDTO transactionRequestDTO) {
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

    @PutMapping("/approve/{id}")
    public ResponseEntity<EntityResponse> approveTransactionRequest(@PathVariable Long id, @RequestParam Long staffId) {
        try {
            TransactionRequest result = transactionRequestService.approveTransactionReQuest(id, staffId);
            EntityResponse<TransactionRequest> response = new EntityResponse<>();
            response.setData(result);
            response.setMessage("Transaction request approved successfully");
            response.setCode(HttpStatus.OK.value());
            response.setStatus(HttpStatus.OK);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            EntityResponse<TransactionRequest> response = new EntityResponse<>();
            response.setData(null);
            response.setMessage("Error approving transaction request: " + e.getMessage());
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setStatus(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/reject/{id}")
    public ResponseEntity<EntityResponse> rejectTransactionRequest(@PathVariable Long id) {
        try {
            TransactionRequest result = transactionRequestService.rejectTransactionReQuest(id);
            EntityResponse<TransactionRequest> response = new EntityResponse<>();
            response.setData(result);
            response.setMessage("Transaction request rejected successfully");
            response.setCode(HttpStatus.OK.value());
            response.setStatus(HttpStatus.OK);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            EntityResponse<TransactionRequest> response = new EntityResponse<>();
            response.setData(null);
            response.setMessage("Error rejecting transaction request: " + e.getMessage());
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setStatus(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
