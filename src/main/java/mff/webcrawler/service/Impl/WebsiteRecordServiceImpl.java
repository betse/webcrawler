package mff.webcrawler.service.Impl;

import mff.webcrawler.model.WebsiteRecord;
import mff.webcrawler.repository.WebsiteRecordRepository;
import mff.webcrawler.service.WebsiteRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WebsiteRecordServiceImpl implements WebsiteRecordService {

    private static final Logger logger = LoggerFactory.getLogger(WebsiteRecordServiceImpl.class);
    private final WebsiteRecordRepository websiteRecordRepository;

    @Autowired
    public WebsiteRecordServiceImpl(WebsiteRecordRepository websiteRecordRepository) {
        this.websiteRecordRepository = websiteRecordRepository;
    }

    @Override
    public List<WebsiteRecord> getAllWebRecords() {
        logger.info("Fetching all website records");
        return websiteRecordRepository.findAll();
    }

    @Override
    public Optional<WebsiteRecord> getRecordById(Long id) {
        logger.info("Fetching website record with ID: {}" + id);
        return websiteRecordRepository.findById(id);
    }

    @Override
    public WebsiteRecord createRecord(WebsiteRecord websiteRecord) {
        logger.info("Creating new website record with URL: {}", websiteRecord.getUrl());
        // Explicitly set lastExecutionTime and lastExecutionStatus to null
        websiteRecord.setLastExecutionTime(null);
        websiteRecord.setLastExecutionStatus(null);
        return websiteRecordRepository.save(websiteRecord);
    }

    @Override
    public Optional<WebsiteRecord> updateRecord(Long id, WebsiteRecord recordDetail) {
        logger.info("Updating website record with ID: {}", id);
        Optional<WebsiteRecord> record = websiteRecordRepository.findById(id);
        if (record.isPresent()) {
            WebsiteRecord updateRecord = record.get();
            updateRecord.setUrl(recordDetail.getUrl());
            updateRecord.setLabel(recordDetail.getLabel());
            updateRecord.setBoundaryRegexp(recordDetail.getBoundaryRegexp());
            updateRecord.setPeriodicity(recordDetail.getPeriodicity());
            updateRecord.setTags(recordDetail.getTags());
            updateRecord.setActive(recordDetail.getActive());
            return Optional.of(websiteRecordRepository.save(updateRecord));

        } else {
            return Optional.empty();
        }
    }

    @Override
    public boolean deleteRecord(Long id) {
        logger.info("Deleting website record with ID: {}", id);
        if (websiteRecordRepository.existsById(id)) {
            websiteRecordRepository.deleteById(id);
            return true;
        } else {
            logger.warn("Website record with ID: {} not found", id);
            return false;
        }
    }
}
