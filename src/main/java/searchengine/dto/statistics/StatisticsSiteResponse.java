package searchengine.dto.statistics;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonPropertyOrder({"error:", "result"})
public class StatisticsSiteResponse {
    private String error;
    private boolean result;

}
