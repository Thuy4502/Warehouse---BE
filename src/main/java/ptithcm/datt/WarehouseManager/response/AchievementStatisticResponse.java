package ptithcm.datt.WarehouseManager.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AchievementStatisticResponse {
    private Long totalCategories;
    private Long totalBooks;
    private String totalImportValue;
    private String totalExportValue;
}
