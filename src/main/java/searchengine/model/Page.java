package searchengine.model;

import javax.persistence.*;

@Entity
@Table(name = "page")
public class Page {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id", columnDefinition = "integer", referencedColumnName = "id", nullable = false)
    private Site site_id;
    @Column(name = "path",columnDefinition = "VARCHAR(512)", nullable = false)
    private String path;
    @Column(name = "code", columnDefinition = "integer", nullable = false)
    private Integer code;
    @Column(name = "content", columnDefinition = "MEDIUMTEXT",nullable = false)
    private String content;

}
