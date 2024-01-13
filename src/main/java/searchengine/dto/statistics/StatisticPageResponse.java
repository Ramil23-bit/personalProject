package searchengine.dto.statistics;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class StatisticPageResponse {
    @NotBlank
    private String url;
    private boolean result;
    private String error;
}
