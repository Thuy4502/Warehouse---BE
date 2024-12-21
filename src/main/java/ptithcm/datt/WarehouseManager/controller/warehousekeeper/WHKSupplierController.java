package ptithcm.datt.WarehouseManager.controller.warehousekeeper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ptithcm.datt.WarehouseManager.model.Book;
import ptithcm.datt.WarehouseManager.model.Supplier;
import ptithcm.datt.WarehouseManager.request.SupplierRequest;
import ptithcm.datt.WarehouseManager.response.EntityResponse;
import ptithcm.datt.WarehouseManager.service.SupplierService;

@RestController
@RequestMapping("v1/warehouse_keeper/supplier")
public class WHKSupplierController {
    @Autowired
    SupplierService supplierService;

    @PostMapping("/add")
    public ResponseEntity<EntityResponse<Supplier>> addSupplier(@RequestBody Supplier supplier) {
        EntityResponse<Supplier> response = new EntityResponse<>();

        try {
            Supplier createSupplier = supplierService.createSupplier(supplier);
            response.setData(createSupplier);
            response.setMessage("Supplier created successfully");
            response.setCode(HttpStatus.CREATED.value());
            response.setStatus(HttpStatus.CREATED);
            return  new ResponseEntity<>(response, response.getStatus());
        }
        catch (Exception e) {
            response.setData(null);
            response.setMessage("Error creating supplier: " + e.getMessage());
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setStatus(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(response, response.getStatus());
        }
    }

    @PutMapping("/update/{supplierId}")
    public ResponseEntity<EntityResponse<Supplier>> updateSupplier(@PathVariable Long supplierId, @RequestBody SupplierRequest supplierRequest) {
        EntityResponse<Supplier> response = new EntityResponse<>();
        try {
            Supplier updateSupplier = supplierService.updateSupplier(supplierId, supplierRequest);
            response.setData(updateSupplier);
            response.setMessage("Supplier updated successfully");
            response.setCode(HttpStatus.OK.value());
            response.setStatus(HttpStatus.OK);
            return new ResponseEntity<>(response, response.getStatus());
        } catch (Exception e) {
            response.setData(null);
            response.setMessage("Error updating supplier: " + e.getMessage());
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setStatus(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(response, response.getStatus());
        }
    }

}
