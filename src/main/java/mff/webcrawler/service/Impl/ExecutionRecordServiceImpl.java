package mff.webcrawler.service.Impl;

import mff.webcrawler.model.ExecutionRecord;
import mff.webcrawler.repository.ExecutionRecordRepository;
import mff.webcrawler.service.ExecutionRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExecutionRecordServiceImpl implements ExecutionRecordService {

    private static final Logger logger = LoggerFactory.getLogger(ExecutionRecordServiceImpl.class);
    private final ExecutionRecordRepository executionRecordRepository;

    @Autowired
    ExecutionRecordServiceImpl(ExecutionRecordRepository executionRecordRepository) {
        this.executionRecordRepository = executionRecordRepository;
    }

    @Override
    public List<ExecutionRecord> getAllExecutionRecords() {
        logger.info("Fetching all execution records");
        return executionRecordRepository.findAll();
    }

    @Override
    public Optional<ExecutionRecord> findById(Long id) {
        logger.info("Fetching execution record with ID: {}", id);
        return executionRecordRepository.findById(id);
    }

    @Override
    public boolean deleteRecord(Long id) {
        logger.info("Deleting execution record with ID: {}", id);
        if (executionRecordRepository.existsById(id)) {
            executionRecordRepository.deleteById(id);
            return true;
        } else {
            logger.warn("Execution record with ID: {} not found", id);
            return false;
        }
    }
}
