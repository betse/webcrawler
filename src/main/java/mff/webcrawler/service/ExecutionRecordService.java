package mff.webcrawler.service;

import mff.webcrawler.model.ExecutionRecord;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing {@link ExecutionRecord} entities.
 */
public interface ExecutionRecordService {

    /**
     * Retrieves all execution records.
     *
     * @return a list of all execution records.
     */
    List<ExecutionRecord> getAllExecutionRecords();


    /**
     * Retrieves an execution record by its ID.
     *
     * @param id the ID of the execution record to retrieve
     * @return an {@code Optional} containing the execution record if found, or empty if not found.
     */
    Optional<ExecutionRecord> findById(Long id);


    /**
     * Deletes an execution record by its ID.
     *
     * @param id the ID of the execution record to delete
     * @return {@code true} if the record was deleted, {@code false} if the record was not found.
     */
    boolean deleteRecord(Long id);

}
