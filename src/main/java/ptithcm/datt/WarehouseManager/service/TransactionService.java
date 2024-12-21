package ptithcm.datt.WarehouseManager.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ptithcm.datt.WarehouseManager.response.AchievementStatisticResponse;
import ptithcm.datt.WarehouseManager.response.MonthlyTransactionResponse;
import ptithcm.datt.WarehouseManager.response.TransactionHistoryResponse;
import ptithcm.datt.WarehouseManager.model.*;
import ptithcm.datt.WarehouseManager.repository.*;
import ptithcm.datt.WarehouseManager.request.TransactionItemRequest;
import ptithcm.datt.WarehouseManager.request.TransactionReq;

import java.sql.Date;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
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

    @Autowired
    private InventoryLogRepository inventoryLogRepository;

    @Autowired
    TransactionItemRepository transactionItemRepository;

    @Transactional
    public Transaction createTransaction(TransactionReq transactionRequest) {
        Transaction createTransaction = new Transaction();
        Type type = typeRepository.findByTypeName(transactionRequest.getType())
                .orElseThrow(() -> new RuntimeException("Type not found"));
        createTransaction.setType(type);

        Staff staff = staffRepository.findById(transactionRequest.getStaffId())
                .orElseThrow(() -> new RuntimeException("Staff not found"));
        createTransaction.setStaff(staff);

        createTransaction.setCreateAt(LocalDateTime.now());
        createTransaction.setDeliveryPerson(transactionRequest.getDeliveryPerson());
        createTransaction.setBusinessPartner(transactionRequest.getBusinessPartner());
        createTransaction.setPhone_number(transactionRequest.getPhone_number());
        createTransaction.setAddress(transactionRequest.getAddress());
        createTransaction.setTaxId(transactionRequest.getTaxId());
        createTransaction.setTotalValue(transactionRequest.getTotalValue());

        if (transactionRequest.getType().equals("Nhập")) {
            Bill newBill = new Bill();
            newBill.setBillCode(transactionRequest.getBillCode());
            billRepository.save(newBill);
            createTransaction.setBill(newBill);
            createTransaction.setTransactionCode(generateImportTransactionCode());
        }
        else {
            createTransaction.setTransactionCode(generateExportTransactionCode());
        }

        if (transactionRequest.getTransactionRequestId() != null) {
            TransactionRequest transactionRequestEntity = transactionRequestRepository.findById(transactionRequest.getTransactionRequestId())
                    .orElseThrow(() -> new RuntimeException("Transaction request not found"));
            createTransaction.setTransactionRequest(transactionRequestEntity);
        }

        transactionRepository.save(createTransaction);

        Set<TransactionItem> transactionItems = new HashSet<>();
        for (TransactionItemRequest transactionItemRequest : transactionRequest.getTransactionItems()) {
            TransactionItem item = new TransactionItem();
            Book book = bookRepository.findById(transactionItemRequest.getBookId())
                    .orElseThrow(() -> new RuntimeException("Book not found"));

            int currentQuantity = book.getQuantity();

            item.setStartQty(currentQuantity);
            item.setBook(book);
            item.setPrice(transactionItemRequest.getPrice());
            item.setActualQuantity(transactionItemRequest.getActualQuantity());
            item.setRequestQuantity(transactionItemRequest.getRequestQuantity());
            item.setTransaction(createTransaction);

            transactionItemRepository.save(item);
            transactionItems.add(item);

            if (transactionRequest.getType().equals("Nhập")) {
                book.setQuantity(currentQuantity + transactionItemRequest.getActualQuantity());
            } else if (transactionRequest.getType().equals("Xuất")) {
                if (currentQuantity < transactionItemRequest.getActualQuantity()) {
                    throw new RuntimeException("Not enough stock for book: " + book.getTitle());
                }
                book.setQuantity(currentQuantity - transactionItemRequest.getActualQuantity());
            }
            bookRepository.save(book);

            InventoryLog inventoryLog = new InventoryLog();
            inventoryLog.setLogDate(LocalDate.now());
            inventoryLog.setStartQuantity(currentQuantity);
            inventoryLog.setStartPrice(book.getPrice());
            inventoryLog.setStartAmount(currentQuantity * book.getPrice());
            int endQuantity;
            if (transactionRequest.getType().equals("Nhập")) {
                inventoryLog.setImportPrice(transactionItemRequest.getPrice());
                inventoryLog.setImportQuantity(transactionItemRequest.getActualQuantity());
                inventoryLog.setImportAmount(transactionItemRequest.getPrice() * transactionItemRequest.getActualQuantity());
                endQuantity = currentQuantity + transactionItemRequest.getActualQuantity();
            } else {
                inventoryLog.setExportPrice(transactionItemRequest.getPrice());
                inventoryLog.setExportQuantity(transactionItemRequest.getActualQuantity());
                inventoryLog.setExportAmount(transactionItemRequest.getPrice() * transactionItemRequest.getActualQuantity());
                endQuantity = currentQuantity - transactionItemRequest.getActualQuantity();
            }

            double endPrice = (book.getPrice() + transactionItemRequest.getPrice()) / 2;
            inventoryLog.setTransactionItem(item);
            inventoryLog.setEndQuantity(endQuantity);
            inventoryLog.setEndPrice(endPrice);
            inventoryLog.setEndAmount(endQuantity * endPrice);
            inventoryLogRepository.save(inventoryLog);
        }
        createTransaction.setTransactionItems(transactionItems);
        TransactionRequest existTransactionReq = transactionRequestRepository.findById(transactionRequest.getTransactionRequestId()).orElseThrow(() -> new RuntimeException("Transaction request not found"));
        existTransactionReq.setStatus("Đã hoàn thành");
        return transactionRepository.save(createTransaction);
    }

    @Transactional
    public Transaction updateTransaction(Long transactionId, TransactionReq transactionRequest) {
        Transaction existingTransaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new EntityNotFoundException("Transaction with ID " + transactionId + " not found"));

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
            Bill bill = billRepository.findByBillCode(transactionRequest.getBillCode())
                    .orElseThrow(() -> new EntityNotFoundException("Bill with code " + transactionRequest.getBillCode() + " not found or multiple bills found"));

            if (!existingTransaction.getBill().equals(bill)) {
                existingTransaction.setBill(bill);
            }

            if (!existingTransaction.getDeliveryPerson().equals(transactionRequest.getDeliveryPerson())) {
                existingTransaction.setDeliveryPerson(transactionRequest.getDeliveryPerson());
            }
        }

        TransactionRequest transactionRequestEntity = transactionRequestRepository.findById(transactionRequest.getTransactionRequestId())
                .orElseThrow(() -> new EntityNotFoundException("Transaction request with ID " + transactionRequest.getTransactionRequestId() + " not found"));

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
                    .orElseThrow(() -> new EntityNotFoundException("Book with ID " + transactionItemRequest.getBookId() + " not found"));

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

            Optional<InventoryLog> optionalInventoryLog = inventoryLogRepository.findByTransactionItemId(transactionItemRequest.getTransactionItemId());
            InventoryLog inventoryLog = optionalInventoryLog.orElseThrow(() -> new EntityNotFoundException("Transaction item with ID " + transactionItemRequest.getTransactionItemId() + " not found"));

            if (transactionRequest.getType().equals("Nhập")) {
                inventoryLog.setImportPrice(transactionItemRequest.getPrice());
                inventoryLog.setImportQuantity(transactionItemRequest.getActualQuantity());
                inventoryLog.setImportAmount(transactionItemRequest.getPrice() * transactionItemRequest.getActualQuantity());
            } else if(transactionRequest.getType().equals("Xuất")) {
                inventoryLog.setExportPrice(transactionItemRequest.getPrice());
                inventoryLog.setExportQuantity(transactionItemRequest.getActualQuantity());
                inventoryLog.setExportAmount(transactionItemRequest.getPrice() * transactionItemRequest.getActualQuantity());
            }

            double bookPrice = book.getPrice() != null ? book.getPrice() : 0.0;
            double endPrice = (bookPrice + transactionItemRequest.getPrice()) / 2;
            int endQuan = book.getQuantity() + transactionItemRequest.getActualQuantity();
            inventoryLog.setTransactionItem(item);
            inventoryLog.setEndQuantity(endQuan);
            inventoryLog.setEndPrice(endPrice);
            inventoryLog.setEndAmount(endQuan * endPrice);
            inventoryLog.setTransactionItem(item);
            inventoryLogRepository.save(inventoryLog);
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



    public List<AchievementStatisticResponse> getBookStatistics() {
        List<Object[]> results = transactionRepository.getBookStatistics();
        if (results == null || results.isEmpty()) {
            return Collections.emptyList();
        }

        DecimalFormat formatter = new DecimalFormat("#,###.##"); // Format with commas and 2 decimal places

        return results.stream()
                .map(result -> new AchievementStatisticResponse(
                        (result[0] instanceof Number) ? ((Number) result[0]).longValue() : 0L,
                        (result[1] instanceof Number) ? ((Number) result[1]).longValue() : 0L,
                        (result[2] instanceof Number) ? formatter.format(((Number) result[2]).doubleValue()) : "0.00",
                        (result[3] instanceof Number) ? formatter.format(((Number) result[3]).doubleValue()) : "0.00"
                ))
                .collect(Collectors.toList());
    }

    private String generateImportTransactionCode() {
        // Lấy năm hiện tại
        String currentYear = String.valueOf(java.time.Year.now().getValue()).substring(2); // Lấy 2 số cuối của năm

        // Lấy tất cả giao dịch
        List<Transaction> transactions = transactionRepository.findAll();
        int maxId = 0;

        // Kiểm tra danh sách giao dịch
        for (Transaction p : transactions) {
            String transactionCode = p.getTransactionCode();
            if (transactionCode.startsWith("PN" + currentYear)) { // Kiểm tra xem có cùng năm không
                String idStr = transactionCode.substring(4); // Loại bỏ "NK" + "21"
                int id = Integer.parseInt(idStr);
                if (id > maxId) {
                    maxId = id;
                }
            }
        }

        // Nếu danh sách rỗng hoặc chưa có mã nào cùng năm, tạo mã đầu tiên
        if (maxId == 0) {
            return String.format("PN%s%06d", currentYear, 1); // Bắt đầu từ 001
        }

        // Nếu đã có mã, tăng số thứ tự lên
        return String.format("PN%s%06d", currentYear, maxId + 1);
    }

    private String generateExportTransactionCode() {
        // Lấy năm hiện tại
        String currentYear = String.valueOf(java.time.Year.now().getValue()).substring(2); // Lấy 2 số cuối của năm

        // Lấy tất cả giao dịch
        List<Transaction> transactions = transactionRepository.findAll();
        int maxId = 0;

        // Kiểm tra danh sách giao dịch
        for (Transaction p : transactions) {
            String transactionCode = p.getTransactionCode();
            if (transactionCode.startsWith("PX" + currentYear)) { // Kiểm tra xem có cùng năm không
                String idStr = transactionCode.substring(4); // Loại bỏ "NK" + "21"
                int id = Integer.parseInt(idStr);
                if (id > maxId) {
                    maxId = id;
                }
            }
        }

        // Nếu danh sách rỗng hoặc chưa có mã nào cùng năm, tạo mã đầu tiên
        if (maxId == 0) {
            return String.format("PX%s%06d", currentYear, 1); // Bắt đầu từ 001
        }

        // Nếu đã có mã, tăng số thứ tự lên
        return String.format("PX%s%06d", currentYear, maxId + 1);
    }

    public List<MonthlyTransactionResponse> getMonthlyTransaction(int year) {
        List<Object[]> results = transactionRepository.findMonthlyRevenueByYear(year);

        DecimalFormat formatter = new DecimalFormat("#,###.##");

        return results.stream()
                .map(result -> new MonthlyTransactionResponse(
                        (result[0] instanceof Number) ? ((Number) result[0]).intValue() : 0,
                        (result[1] instanceof Number) ? formatter.format(((Number) result[1]).doubleValue()) : "0.00",
                        (result[2] instanceof Number) ? formatter.format(((Number) result[2]).doubleValue()) : "0.00"
                ))
                .collect(Collectors.toList());
    }




}
