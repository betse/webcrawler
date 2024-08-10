package mff.webcrawler.service.Impl;

import mff.webcrawler.Enums.Periodicity;
import mff.webcrawler.model.WebsiteRecord;
import mff.webcrawler.repository.WebsiteRecordRepository;
import mff.webcrawler.service.SchedulerService;
import mff.webcrawler.service.WebCrawlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementation of the {@link SchedulerService} that manages the scheduling
 * of web crawling tasks. This service periodically checks for active
 * {@link WebsiteRecord} entities and triggers the crawling process for those
 * that need to be crawled based on their configured periodicity.
 */
@Service
public class SchedulerServiceImpl implements SchedulerService {
    private static final Logger logger = LoggerFactory.getLogger(SchedulerServiceImpl.class);
    private final WebsiteRecordRepository websiteRecordRepository;
    private final WebCrawlerService webCrawlerService;


    /**
     * Constructs a new {@code SchedulerServiceImpl} with the specified
     * {@link WebsiteRecordRepository} and {@link WebCrawlerService}.
     *
     * @param websiteRecordRepository the repository for accessing {@link WebsiteRecord} entities
     * @param webCrawlerService the service responsible for executing the web crawling process
     */
    @Autowired
    public SchedulerServiceImpl(WebsiteRecordRepository websiteRecordRepository, WebCrawlerService webCrawlerService) {
        this.websiteRecordRepository = websiteRecordRepository;
        this.webCrawlerService = webCrawlerService;
    }

    @Override
    @Scheduled(fixedRate = 60000)
    public void scheduleCrawling() {
        logger.info("==================================");
        logger.info("Starting scheduled crawling task");
        logger.info("==================================");
        List<WebsiteRecord> activeRecords = websiteRecordRepository.findByActiveTrue();
        for (WebsiteRecord record : activeRecords) {
            if (shouldCrawl(record)) {
                webCrawlerService.crawl(record);
            }
        }
        logger.info("Scheduled crawling task completed");
    }


    /**
     * Determines if a given {@link WebsiteRecord} should be crawled based on its
     * last execution time and the configured periodicity.
     *
     * @param websiteRecord the website record to check
     * @return {@code true} if the record should be crawled; {@code false} otherwise
     */
    private boolean shouldCrawl(WebsiteRecord websiteRecord) {
        LocalDateTime lastCrawlTime = websiteRecord.getLastExecutionTime();
        if (lastCrawlTime == null) {
            return true;
        }
        LocalDateTime nextCrawlTime = lastCrawlTime.plus(getPeriodDuration(websiteRecord.getPeriodicity()));
        return LocalDateTime.now().isAfter(nextCrawlTime);
    }

    /**
     * Converts a {@link Periodicity} value into a corresponding {@link Duration} object.
     * This method maps the periodicity enum to a specific time duration that determines
     * how frequently a website record should be crawled.
     *
     * @param periodicity the periodicity of the website record
     * @return the corresponding duration for the given periodicity
     * @throws IllegalArgumentException if the periodicity value is invalid
     */
    private Duration getPeriodDuration(Periodicity periodicity) {
        switch (periodicity) {
            case Minute:
                return Duration.ofMinutes(1);
            case Hour:
                return Duration.ofHours(1);
            case Day:
                return Duration.ofDays(1);
            default:
                throw new IllegalArgumentException("Invalid Periodicity: " + periodicity);
        }
    }
}
