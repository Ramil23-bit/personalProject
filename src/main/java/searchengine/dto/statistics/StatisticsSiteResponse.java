package searchengine.dto.statistics;

import lombok.Data;

@Data
public class StatisticsSiteResponse {
    private boolean result;
    private String outputResult;
    private String outputError;
}
