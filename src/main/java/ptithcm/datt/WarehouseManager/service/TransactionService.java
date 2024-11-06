package ptithcm.datt.WarehouseManager.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ptithcm.datt.WarehouseManager.dto.response.TransactionHistoryResponse;
import ptithcm.datt.WarehouseManager.model.*;
import ptithcm.datt.WarehouseManager.repository.*;
import ptithcm.datt.WarehouseManager.request.TransactionItemRequest;
import ptithcm.datt.WarehouseManager.request.TransactionReq;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TypeRepository typeRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private TransactionRequestRepository transactionRequestRepository;

    public Transaction createTransaction(TransactionReq transactionRequest) {
        Transaction createTransaction = new Transaction();
        Type type = typeRepository.findByTypeName(transactionRequest.getType())
                .orElseThrow(() -> new RuntimeException("Type not found"));
        createTransaction.setType(type);
        Staff staff = staffRepository.findById(transactionRequest.getStaffId())
                .orElseThrow(() -> new RuntimeException("Staff not found"));
        createTransaction.setStaff(staff);
        createTransaction.setType(type);
        createTransaction.setCreateAt(LocalDateTime.now());
        createTransaction.setBusinessPartner(transactionRequest.getBusinessPartner());
        createTransaction.setPhone_number(transactionRequest.getPhone_number());
        createTransaction.setAddress(transactionRequest.getAddress());
        createTransaction.setTaxId(transactionRequest.getTaxId());
        createTransaction.setTotalValue(transactionRequest.getTotalValue());
        createTransaction.setTransactionCode(transactionRequest.getTransactionCode());
        if(transactionRequest.getType().equals("Nhập")) {
            Bill bill = billRepository.findById(transactionRequest.getBillId())
                    .orElseThrow(() -> new RuntimeException("Bill not found"));
            createTransaction.setBill(bill);
        }
        TransactionRequest transactionRequest1 = transactionRequestRepository.findById(transactionRequest.getTransactionRequestId())
                .orElseThrow(() -> new RuntimeException("Transaction request not found"));
        createTransaction.setTransactionRequest(transactionRequest1);

        Set<TransactionItem> transactionItems = new HashSet<>();
        for (TransactionItemRequest transactionItemRequest : transactionRequest.getTransactionItems()) {
            TransactionItem item = new TransactionItem();
            Book book = bookRepository.findById(transactionItemRequest.getBookId())
                    .orElseThrow(() -> new RuntimeException("Book not found"));
            item.setStartQty(book.getQuantity());
            item.setBook(book);
            item.setActualQuantity(transactionItemRequest.getActualQuantity());
            item.setRequestQuantity(transactionItemRequest.getRequestQuantity());
            item.setPrice(transactionItemRequest.getPrice());
            item.setTransaction(createTransaction);
            transactionItems.add(item);
            if (transactionRequest.getType().equals("Nhập")) {
                book.setQuantity(book.getQuantity() + transactionItemRequest.getActualQuantity());
            } else if (transactionRequest.getType().equals("Xuất")) {
                if (book.getQuantity() < transactionItemRequest.getActualQuantity()) {
                    throw new RuntimeException("Not enough stock for book: " + book.getTitle());
                }
                book.setQuantity(book.getQuantity()- transactionItemRequest.getActualQuantity());
            }
            bookRepository.save(book);
        }

        createTransaction.setTransactionItems(transactionItems);
        return transactionRepository.save(createTransaction);
    }

    @Transactional
    public Transaction updateTransaction(Long transactionId, TransactionReq transactionRequest) {
        Transaction existingTransaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (!existingTransaction.getBusinessPartner().equals(transactionRequest.getBusinessPartner())) {
            existingTransaction.setBusinessPartner(transactionRequest.getBusinessPartner());
        }

        if (!existingTransaction.getPhone_number().equals(transactionRequest.getPhone_number())) {
            existingTransaction.setPhone_number(transactionRequest.getPhone_number());
        }

        if (!existingTransaction.getAddress().equals(transactionRequest.getAddress())) {
            existingTransaction.setAddress(transactionRequest.getAddress());
        }

        if (!existingTransaction.getTotalValue().equals(transactionRequest.getTotalValue())) {
            existingTransaction.setTotalValue(transactionRequest.getTotalValue());
        }

        if (transactionRequest.getType().equals("Nhập")) {
            Bill bill = billRepository.findById(transactionRequest.getBillId())
                    .orElseThrow(() -> new RuntimeException("Bill not found"));
            if (!existingTransaction.getBill().equals(bill)) {
                existingTransaction.setBill(bill);
            }
        }

        TransactionRequest transactionRequestEntity = transactionRequestRepository.findById(transactionRequest.getTransactionRequestId())
                .orElseThrow(() -> new RuntimeException("Transaction request not found"));
        if (!existingTransaction.getTransactionRequest().equals(transactionRequestEntity)) {
            existingTransaction.setTransactionRequest(transactionRequestEntity);
        }

        Set<TransactionItem> existingTransactionItems = existingTransaction.getTransactionItems();
        Set<TransactionItem> updatedTransactionItems = new HashSet<>();

        for (TransactionItemRequest transactionItemRequest : transactionRequest.getTransactionItems()) {
            TransactionItem item = existingTransactionItems.stream()
                    .filter(ti -> ti.getBook().getBookId().equals(transactionItemRequest.getBookId()))
                    .findFirst()
                    .orElse(new TransactionItem());

            Book book = bookRepository.findById(transactionItemRequest.getBookId())
                    .orElseThrow(() -> new RuntimeException("Book not found"));

            if (!item.getBook().equals(book)) {
                item.setBook(book);
            }

            if (item.getActualQuantity() != transactionItemRequest.getActualQuantity()) {
                item.setActualQuantity(transactionItemRequest.getActualQuantity());
            }

            if (item.getRequestQuantity() != transactionItemRequest.getRequestQuantity()) {
                item.setRequestQuantity(transactionItemRequest.getRequestQuantity());
            }

            if (!item.getPrice().equals(transactionItemRequest.getPrice())) {
                item.setPrice(transactionItemRequest.getPrice());
            }

            item.setTransaction(existingTransaction);
            updatedTransactionItems.add(item);
        }

        existingTransaction.setTransactionItems(updatedTransactionItems);
        return transactionRepository.save(existingTransaction);
    }

    public List<Transaction> getAllTransaction(String type) {

        return transactionRepository.findByTypeName(type);
    }

    public List<TransactionHistoryResponse> getTransactionHistory(String type) {
        List<Object[]> results = transactionRepository.transactionHistory(type);

        return results.stream().map(result -> new TransactionHistoryResponse(
                (Date) result[0],   // createDate
                (String) result[1],   // transactionCode
                (Long) result[2],     // bookId
                (String) result[3],   // bookName
                (double) result[4],   // price
                (int) result[5],      // startQty
                (int) result[6],       // actualQuantity
                (Long) result[7]
        )).collect(Collectors.toList());
    }
}
