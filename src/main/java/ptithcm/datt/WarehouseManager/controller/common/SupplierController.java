package ptithcm.datt.WarehouseManager.controller.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ptithcm.datt.WarehouseManager.model.Book;
import ptithcm.datt.WarehouseManager.model.Supplier;
import ptithcm.datt.WarehouseManager.response.EntityResponse;
import ptithcm.datt.WarehouseManager.service.SupplierService;

import java.util.List;

@RestController
@RequestMapping("v1/api/supplier")
public class SupplierController {
    @Autowired
    SupplierService supplierService;

    @GetMapping("/getAll")
    public ResponseEntity<EntityResponse<List<Supplier>>> getAllSupplier() {
        List<Supplier> supplierList = supplierService.getAllSupplier();
        EntityResponse<List<Supplier>> response = new EntityResponse<>();
        response.setData(supplierList);
        response.setMessage("Get all supplier successfully");
        response.setCode(HttpStatus.OK.value());
        response.setStatus(HttpStatus.OK);
        return new ResponseEntity<>(response, response.getStatus());
    }


}
