package ptithcm.datt.WarehouseManager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="staff")
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="staff_id")
    private Long staffId;
    @Column(name="staff_name")
    private String staffName;

    private String address;
    private String email;
    @Column(name="phone_number")
    private String phoneNumber;
    private Date dob;
    @Column(name="hired_date")
    private LocalDateTime hiredDate;
    private String img;

    @Column(name="is_enable")
    private Boolean isEnable;

    @OneToOne(mappedBy = "staff")
    private Account account;

//    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
//    private List<Book> books;

    @JsonIgnore
    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Category> categories;

    @JsonIgnore
    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TransactionRequest> transactionRequests;

    @JsonIgnore
    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactions;




}
