package searchengine.model;

import javax.persistence.Column;

public enum EnumForTable {
    INDEXING,
    INDEXED,
    FAILED
//    @Column(columnDefinition = "ENUM('INDEXING', 'INDEXED')"),
//    @Column(columnDefinition = "VARCHAR(255)"),
//    @Column(columnDefinition = "TEXT"),
//    @Column(columnDefinition = "MEDIUMTEXT")
}
