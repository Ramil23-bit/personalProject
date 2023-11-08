package searchengine.model;

import javax.persistence.*;

@Entity
@Table(name = "lemma")
public class Lemma {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "site_id", nullable = false)
    private Integer siteId;
    @Column(name = "lemma", columnDefinition = "VARCHAR(255)", nullable = false)
    private String lemma;
    @Column(name = "frequency", nullable = false)
    private Integer frequency;
}
