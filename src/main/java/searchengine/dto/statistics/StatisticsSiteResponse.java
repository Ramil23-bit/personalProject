package searchengine.dto.statistics;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
public class StatisticsSiteResponse {
    private String error;
    private boolean result;

}
