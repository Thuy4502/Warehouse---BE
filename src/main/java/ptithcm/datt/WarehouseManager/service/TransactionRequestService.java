package ptithcm.datt.WarehouseManager.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ptithcm.datt.WarehouseManager.model.*;
import ptithcm.datt.WarehouseManager.repository.BookRepository;
import ptithcm.datt.WarehouseManager.repository.StaffRepository;
import ptithcm.datt.WarehouseManager.repository.TransactionRequestRepository;
import ptithcm.datt.WarehouseManager.repository.TypeRepository;
import ptithcm.datt.WarehouseManager.request.TransactionRequestDTO;
import ptithcm.datt.WarehouseManager.request.TransactionRequestItemRequest;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TransactionRequestService {
    @Autowired
    private TransactionRequestRepository transactionRequestRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TypeRepository typeRepository;

    public List<TransactionRequest> getAllTransactionRequestByType(String type) {
        if (type != null && !type.isEmpty()) {
            return transactionRequestRepository.findByType(type);
        }
        return null;
    }
    public TransactionRequest createTransactionRequest(TransactionRequestDTO transactionRequestDTO) {
        TransactionRequest createdTransactionRequest = new TransactionRequest();
        createdTransactionRequest.setCreateBy(transactionRequestDTO.getCreateBy());
        createdTransactionRequest.setPhoneNumber(transactionRequestDTO.getPhoneNumber());
        createdTransactionRequest.setPosition(transactionRequestDTO.getPosition());
        createdTransactionRequest.setReason(transactionRequestDTO.getReason());
        createdTransactionRequest.setDepartment(transactionRequestDTO.getDepartment());
        createdTransactionRequest.setTotalValue(transactionRequestDTO.getTotalValue());
        createdTransactionRequest.setTransactionRequestCode(transactionRequestDTO.getTransactionRequestCode());
        createdTransactionRequest.setCreateAt(LocalDateTime.now());
        createdTransactionRequest.setStatus("CREATED");
        Staff staff = staffRepository.findById(transactionRequestDTO.getStaffId())
                .orElseThrow(() -> new RuntimeException("Staff not found"));
        createdTransactionRequest.setStaff(staff);

        Type type = typeRepository.findByTypeName(transactionRequestDTO.getType())
                .orElseThrow(() -> new RuntimeException("Type not found"));
        createdTransactionRequest.setType(type);

        Set<TransactionRequestItem> transactionRequestItems = new HashSet<>();
        for (TransactionRequestItemRequest itemDTO : transactionRequestDTO.getTransactionRequestItems()) {
            TransactionRequestItem item = new TransactionRequestItem();
            Book book = bookRepository.findById(itemDTO.getBookId())
                    .orElseThrow(() -> new RuntimeException("Book not found"));
            item.setBook(book);
            item.setQuantity(itemDTO.getQuantity());
            item.setPrice(itemDTO.getPrice());
            item.setTransactionRequest(createdTransactionRequest);
            transactionRequestItems.add(item);
        }

        createdTransactionRequest.setTransactionRequestItems(transactionRequestItems);
        return transactionRequestRepository.save(createdTransactionRequest);
    }

    @Transactional
    public TransactionRequest updateTransactionRequest(Long id, TransactionRequestDTO transactionRequestDTO) {
        TransactionRequest existingTransactionRequest = transactionRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction request not found"));

        if (transactionRequestDTO.getCreateBy() != null &&
                !transactionRequestDTO.getCreateBy().equals(existingTransactionRequest.getCreateBy())) {
            existingTransactionRequest.setCreateBy(transactionRequestDTO.getCreateBy());
        }

        if (transactionRequestDTO.getStaffId() != null) {
            if (existingTransactionRequest.getStaffUpdate() == null ||
                    !existingTransactionRequest.getStaffUpdate().getStaffId().equals(transactionRequestDTO.getUpdateBy())) {
                Staff staff = staffRepository.findById(transactionRequestDTO.getUpdateBy())
                        .orElseThrow(() -> new RuntimeException("Staff not found"));
                existingTransactionRequest.setStaffUpdate(staff);
            }
        }

        if (transactionRequestDTO.getStatus() != null &&
                !transactionRequestDTO.getStatus().equals(existingTransactionRequest.getStatus())) {
            existingTransactionRequest.setStatus(transactionRequestDTO.getStatus());
        }

        if (transactionRequestDTO.getReason() != null &&
                !transactionRequestDTO.getReason().equals(existingTransactionRequest.getReason())) {
            existingTransactionRequest.setReason(transactionRequestDTO.getReason());
        }

        if (transactionRequestDTO.getPosition() != null &&
                !transactionRequestDTO.getPosition().equals(existingTransactionRequest.getPosition())) {
            existingTransactionRequest.setPosition(transactionRequestDTO.getPosition());
        }

        if (transactionRequestDTO.getTransactionRequestCode() != null &&
                !transactionRequestDTO.getTransactionRequestCode().equals(existingTransactionRequest.getTransactionRequestCode())) {
            existingTransactionRequest.setTransactionRequestCode(transactionRequestDTO.getTransactionRequestCode());
        }
        if (transactionRequestDTO.getDepartment() != null &&
                !transactionRequestDTO.getDepartment().equals(existingTransactionRequest.getDepartment())) {
            existingTransactionRequest.setDepartment(transactionRequestDTO.getDepartment());
        }

        Set<TransactionRequestItem> updatedTransactionRequestItems = new HashSet<>();
        for (TransactionRequestItemRequest itemDTO : transactionRequestDTO.getTransactionRequestItems()) {
            Optional<TransactionRequestItem> existingItemOptional = existingTransactionRequest.getTransactionRequestItems()
                    .stream()
                    .filter(item -> item.getBook().getBookId().equals(itemDTO.getBookId()))
                    .findFirst();

            TransactionRequestItem item;
            if (existingItemOptional.isPresent()) {
                item = existingItemOptional.get();
                if (item.getQuantity() != itemDTO.getQuantity()) {
                    item.setQuantity(itemDTO.getQuantity());
                }
                if (!item.getPrice().equals(itemDTO.getPrice())) {
                    item.setPrice(itemDTO.getPrice());
                }
            } else {
                item = new TransactionRequestItem();
                Book book = bookRepository.findById(itemDTO.getBookId())
                        .orElseThrow(() -> new RuntimeException("Book not found"));
                item.setBook(book);
                item.setQuantity(itemDTO.getQuantity());
                item.setPrice(itemDTO.getPrice());
                item.setTransactionRequest(existingTransactionRequest);
            }
            updatedTransactionRequestItems.add(item);
        }

        existingTransactionRequest.getTransactionRequestItems().clear();
        existingTransactionRequest.getTransactionRequestItems().addAll(updatedTransactionRequestItems);

        return transactionRequestRepository.save(existingTransactionRequest);
    }





}
