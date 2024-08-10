package mff.webcrawler.service;

import mff.webcrawler.model.WebsiteRecord;

/**
 * Service interface for managing the web crawling process.
 */
public interface WebCrawlerService {

    /**
     * Initiates the crawling process for the given website record.
     *
     * @param record the {@link WebsiteRecord} entity that contains the information needed to perform the crawl.
     */
    void crawl(WebsiteRecord record);

}
