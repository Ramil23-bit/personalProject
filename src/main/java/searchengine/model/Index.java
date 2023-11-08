package searchengine.model;

import javax.persistence.*;

@Entity
@Table(name = "index")
public class Index {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "page_id", nullable = false)
    private int pageId;
    @Column(name = "lemma_id", nullable = false)
    private int lemmaId;
    @Column(name = "rank", nullable = false)
    private float rank;
}
