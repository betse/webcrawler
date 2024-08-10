package mff.webcrawler.repository;

import mff.webcrawler.model.ExecutionRecord;
import mff.webcrawler.model.WebsiteRecord;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing {@link ExecutionRecord} entities.
 */
public interface ExecutionRecordRepository extends JpaRepository<ExecutionRecord, Long> {
}
