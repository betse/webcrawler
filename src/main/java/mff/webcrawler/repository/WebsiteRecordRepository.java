package mff.webcrawler.repository;

import mff.webcrawler.model.WebsiteRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for managing {@link WebsiteRecord} entities.
 */
public interface WebsiteRecordRepository extends JpaRepository<WebsiteRecord, Long> {

    /**
     * Finds all active website records.
     *
     * @return a list of active website records.
     */
    List<WebsiteRecord> findByActiveTrue();
}
