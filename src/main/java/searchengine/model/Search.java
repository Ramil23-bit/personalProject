package searchengine.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "index")
public class Search {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "page_id", nullable = false, foreignKey = @ForeignKey(name = "FK_search_page"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Page pageId;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "lemma_id", nullable = false, foreignKey = @ForeignKey(name = "FK_search_lemma"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Lemma lemmaId;
    @Column(name = "percent_lemma", nullable = false)
    private float percentLemma;
}
