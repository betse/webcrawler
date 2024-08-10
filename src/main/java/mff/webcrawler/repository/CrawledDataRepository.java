package mff.webcrawler.repository;

import mff.webcrawler.model.CrawledData;
import mff.webcrawler.model.WebsiteRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for managing {@link CrawledData} entities.
 * This interface provides methods for performing CRUD operations
 * and custom queries related to {@link CrawledData}.
 */
public interface CrawledDataRepository extends JpaRepository<CrawledData, Long> {

    /**
     * Finds all {@link CrawledData} entities that are associated with a specific {@link WebsiteRecord}.
     *
     * @param owner the {@link WebsiteRecord} that owns the crawled data
     * @return a list of crawled data entities associated with the given owner
     */
    List<CrawledData> findByOwner(WebsiteRecord owner);

    /**
     * Finds all {@link CrawledData} entities associated with a list of {@link WebsiteRecord} IDs.
     *
     * @param ownerIds a list of IDs of the {@link WebsiteRecord} entities
     * @return a list of crawled data entities associated with the given owner IDs
     */
    List<CrawledData> findAllByOwnerIdIn(List<Long> ownerIds);

    /**
     * Deletes all provided {@link CrawledData} entities.
     *
     * @param entities an {@link Iterable} of crawled data entities to delete
     */
    void deleteAll(Iterable<? extends CrawledData> entities);
}
