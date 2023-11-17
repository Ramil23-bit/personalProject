package searchengine.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.annotation.processing.Generated;
import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
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
    //@CreationTimestamp
    @UpdateTimestamp
    private Instant status_time;
    @Column(name = "last_error", columnDefinition = "TEXT" ,nullable = false)
    private String last_error;
    @Column(name = "url", columnDefinition = "VARCHAR(255)" ,nullable = false)
    private String url;
    @Column(name = "name_site", columnDefinition = "VARCHAR(255)" ,nullable = false)
    private String nameSite;
    @OneToMany(mappedBy = "siteId", fetch = FetchType.LAZY)
    private List<Page> pageList = new ArrayList<>();

    @OneToMany(mappedBy = "siteId", fetch = FetchType.LAZY)
    private List<Lemma> lemmaList = new ArrayList<>();

}
