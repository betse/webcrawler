package mff.webcrawler.service;

import org.springframework.scheduling.annotation.Scheduled;

/**
 * Service interface for scheduling the web crawling tasks.
 */
public interface SchedulerService {

    /**
     * Schedules the crawling tasks to run at a fixed rate.
     * This method is automatically invoked by the Spring Scheduler every 60 seconds.
     */
    @Scheduled(fixedRate = 60000)
    void scheduleCrawling();
}
