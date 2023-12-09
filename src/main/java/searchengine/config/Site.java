package searchengine.config;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
public class Site {
    private String url;
    private String name;
    private Instant statusTime;
}
