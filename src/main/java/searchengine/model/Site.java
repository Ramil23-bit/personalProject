package searchengine.model;

import lombok.Getter;
import lombok.Setter;

import javax.annotation.processing.Generated;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "site")
@Getter
@Setter
public class Site {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EnumForTable status;
    @Column(name = "status_time", nullable = false)
    private Date status_time;
    @Column(name = "last_error", columnDefinition = "TEXT" ,nullable = false)
    private String last_error;
    @Column(name = "url", columnDefinition = "VARCHAR(255)" ,nullable = false)
    private String url;
    @Column(name = "name_site", columnDefinition = "VARCHAR(255)" ,nullable = false)
    private String nameSite;

}
