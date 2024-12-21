package ptithcm.datt.WarehouseManager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ptithcm.datt.WarehouseManager.model.Supplier;
import ptithcm.datt.WarehouseManager.repository.SupplierRepository;
import ptithcm.datt.WarehouseManager.request.SupplierRequest;

import java.util.List;

@Service
public class SupplierService {
    @Autowired
    SupplierRepository supplierRepository;

    public List<Supplier> getAllSupplier() {
        return supplierRepository.findAll();
    }

    public Supplier createSupplier(Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    public Supplier updateSupplier(Long supplierId, SupplierRequest supplierRequest) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(()-> new RuntimeException("Supplier not found"));

        if (supplierRequest.getSupplierName() != null &&
                !supplierRequest.getSupplierName().equals(supplier.getSupplierName())) {
            supplier.setSupplierName(supplierRequest.getSupplierName());
        }

        if (supplierRequest.getEmail() != null &&
                !supplierRequest.getEmail().equals(supplier.getEmail())) {
            supplier.setEmail(supplierRequest.getEmail());
        }

        if (supplierRequest.getAddress() != null &&
                !supplierRequest.getAddress().equals(supplier.getAddress())) {
            supplier.setAddress(supplierRequest.getAddress());
        }

        if (supplierRequest.getPhoneNumber() != null &&
                !supplierRequest.getPhoneNumber().equals(supplier.getPhoneNumber())) {
            supplier.setPhoneNumber(supplierRequest.getPhoneNumber());
        }


        return supplierRepository.save(supplier);
    }

}
