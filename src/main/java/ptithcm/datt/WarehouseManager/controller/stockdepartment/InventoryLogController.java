package ptithcm.datt.WarehouseManager.controller.stockdepartment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ptithcm.datt.WarehouseManager.model.Book;
import ptithcm.datt.WarehouseManager.model.InventoryLog;
import ptithcm.datt.WarehouseManager.model.Transaction;
import ptithcm.datt.WarehouseManager.response.EntityResponse;
import ptithcm.datt.WarehouseManager.response.InventoryLogResponse;
import ptithcm.datt.WarehouseManager.response.InventoryResponse;
import ptithcm.datt.WarehouseManager.service.InventoryLogService;

import java.util.List;

@RestController
@RequestMapping("/v1/stockdepartment/inventory_log")
public class InventoryLogController {
    @Autowired
    InventoryLogService inventoryLogService;

    @GetMapping("/getAll/{type}")
    public ResponseEntity<EntityResponse> getAllInventoryLog(@PathVariable String type)
    {
        List<InventoryLogResponse> inventoryLogs = inventoryLogService.getAllInventoryLogs(type);
        EntityResponse<List<InventoryLogResponse>> response = new EntityResponse<>();
        response.setData(inventoryLogs);
        response.setMessage("Get all inventory log successfully");
        response.setCode(HttpStatus.OK.value());
        response.setStatus(HttpStatus.OK);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping("/inventory-summary")
    public ResponseEntity<EntityResponse<List<InventoryResponse>>>  getInventorySummary(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate) {
        List<InventoryResponse> inventoryReport = inventoryLogService.getInventorySummary(startDate, endDate);
        EntityResponse<List<InventoryResponse>> response = new EntityResponse<>();
        response.setData(inventoryReport);
        response.setMessage("Get all inventory report successfully");
        response.setCode(HttpStatus.OK.value());
        response.setStatus(HttpStatus.OK);
        return new ResponseEntity<>(response, response.getStatus());
    }




}
