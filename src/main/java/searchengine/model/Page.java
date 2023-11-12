package searchengine.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "page")
public class Page {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id", nullable = false, foreignKey = @ForeignKey(name = "FK_page_site"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Site siteId;
    @Column(name = "path",columnDefinition = "VARCHAR(512)", nullable = false)
    private String path;
    @Column(name = "code", columnDefinition = "integer", nullable = false)
    private Integer code;
    @Column(name = "content", columnDefinition = "MEDIUMTEXT",nullable = false)
    private String content;
    @OneToMany(mappedBy = "pageId", fetch = FetchType.LAZY)
    private List<Search> searchList = new ArrayList<>();


}
