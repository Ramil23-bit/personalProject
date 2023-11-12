package searchengine.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import searchengine.model.Search;
@Repository
public interface SearchRepository extends CrudRepository<Search, Integer> {
}
