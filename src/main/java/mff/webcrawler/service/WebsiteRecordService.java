package mff.webcrawler.service;

import mff.webcrawler.model.WebsiteRecord;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing {@link WebsiteRecord} entities.
 */
public interface WebsiteRecordService {

    /**
     * Retrieves all website records.
     *
     * @return a list of all website records.
     */
    List<WebsiteRecord> getAllWebRecords();

    /**
     * Retrieves a website record by its ID.
     *
     * @param id the ID of the website record
     * @return an {@code Optional} containing the website record, or empty if not found.
     */
    Optional<WebsiteRecord> getRecordById(Long id);

    /**
     * Creates a new website record.
     *
     * @param websiteRecord the website record to create
     * @return the created website record
     */
    WebsiteRecord createRecord(WebsiteRecord websiteRecord);

    /**
     * Updates an existing website record.
     *
     * @param id            the ID of the website record to update
     * @param websiteRecord the updated website record details
     * @return an {@code Optional} containing the updated website record, or empty if not found.
     */
    Optional<WebsiteRecord> updateRecord(Long id, WebsiteRecord websiteRecord);

    /**
     * Deletes a website record by its ID.
     *
     * @param id the ID of the website record to delete
     * @return {@code true} if the record was deleted, {@code false} if not found.
     */
    boolean deleteRecord(Long id);

}
