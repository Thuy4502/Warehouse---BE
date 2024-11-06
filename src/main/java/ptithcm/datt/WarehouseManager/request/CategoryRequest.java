package ptithcm.datt.WarehouseManager.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.StandardException;
import ptithcm.datt.WarehouseManager.model.Book;
import ptithcm.datt.WarehouseManager.model.Staff;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CategoryRequest {
    private String categoryName;
    private String description;
    private Long staffId;
}
