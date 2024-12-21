package ptithcm.datt.WarehouseManager.controller.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ptithcm.datt.WarehouseManager.response.*;
import ptithcm.datt.WarehouseManager.service.BookService;
import ptithcm.datt.WarehouseManager.service.TransactionService;

import java.util.List;

@RestController
@RequestMapping("v1/api/statistic")
public class StatisticController {
    @Autowired
    TransactionService transactionService;

    @Autowired
    BookService bookService;

    @GetMapping("/achievement")
    public ResponseEntity<EntityResponse> getTransactionHistory() {
        List<AchievementStatisticResponse> achievementStatistic = transactionService.getBookStatistics();
        EntityResponse<List<AchievementStatisticResponse>> response = new EntityResponse<>();
        response.setData(achievementStatistic);
        response.setMessage("Get achievement statistic request successfully");
        response.setCode(HttpStatus.OK.value());
        response.setStatus(HttpStatus.OK);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping("/monthly_transaction/{year}")
    public ResponseEntity<EntityResponse<List<MonthlyTransactionResponse>>> getMonthlyTransaction(@PathVariable(value = "year") int year) {
        List<MonthlyTransactionResponse> monthlyTransactions = transactionService.getMonthlyTransaction(year);
        EntityResponse<List<MonthlyTransactionResponse>> response = new EntityResponse<>();
        response.setData(monthlyTransactions);
        response.setMessage("Get monthly transaction request successfully");
        response.setCode(HttpStatus.OK.value());
        response.setStatus(HttpStatus.OK);

        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping("/top_selling_books")
    public ResponseEntity<EntityResponse<List<TopSellingBooksResponse>>> getTopSellingBooks() {
        List<TopSellingBooksResponse> topSellingBooksResponseList = bookService.getTop3SellingBooks();
        EntityResponse<List<TopSellingBooksResponse>> response = new EntityResponse<>();
        response.setData(topSellingBooksResponseList);
        response.setMessage("Get top selling books successfully");
        response.setCode(HttpStatus.OK.value());
        response.setStatus(HttpStatus.OK);
        return new ResponseEntity<>(response, response.getStatus());
    }

}
