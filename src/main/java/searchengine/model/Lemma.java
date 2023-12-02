package searchengine.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lemma")
public class Lemma {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "site_id", nullable = false, foreignKey = @ForeignKey(name = "FK_lemma_site"))
        @OnDelete(action = OnDeleteAction.CASCADE)
        private Site siteId;
        @Column(name = "lemma", columnDefinition = "VARCHAR(255)", nullable = false)
        private String lemma;
        @Column(name = "frequency", nullable = false)
        private Integer frequency;
        @OneToMany(mappedBy = "lemmaId", fetch = FetchType.LAZY)
        private List<Search> searchList = new ArrayList<>();
}
