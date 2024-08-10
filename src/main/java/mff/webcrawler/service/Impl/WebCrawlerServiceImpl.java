package mff.webcrawler.service.Impl;

import mff.webcrawler.Enums.Status;
import mff.webcrawler.model.CrawledData;
import mff.webcrawler.model.ExecutionRecord;
import mff.webcrawler.model.WebsiteRecord;
import mff.webcrawler.repository.ExecutionRecordRepository;
import mff.webcrawler.repository.CrawledDataRepository;
import mff.webcrawler.repository.WebsiteRecordRepository;
import mff.webcrawler.service.WebCrawlerService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

/**
 * Implementation of the {@link WebCrawlerService} that manages the web crawling process.
 * This service crawls websites, extracts data, and records the results.
 */
@Service
@EnableScheduling
public class WebCrawlerServiceImpl implements WebCrawlerService {

    private final Logger logger = LoggerFactory.getLogger(WebCrawlerServiceImpl.class);
    private static final Set<String> UNSUPPORTED_SCHEMES = Set.of("mailto:", "tel:");
    private final CrawledDataRepository crawledDataRepository;
    private final ExecutionRecordRepository executionRecordRepository;
    private final WebsiteRecordRepository websiteRecordRepository;
    private final ExecutorService threadPool;
    private Set<String> visitedUrls;
    private Queue<String> urlsToCrawl;
    private static final int MAX_DEPTH = 5;
    private static final int MAX_CONCURRENT_TASKS = 100;


    /**
     * Constructs a new {@code WebCrawlerServiceImpl} with the specified repositories.
     *
     * @param crawledDataRepository     the repository for managing crawled data
     * @param executionRecordRepository the repository for managing execution records
     * @param websiteRecordRepository   the repository for managing website records
     */
    @Autowired
    public WebCrawlerServiceImpl(CrawledDataRepository crawledDataRepository,
                                 ExecutionRecordRepository executionRecordRepository,
                                 WebsiteRecordRepository websiteRecordRepository) {
        this.crawledDataRepository = crawledDataRepository;
        this.executionRecordRepository = executionRecordRepository;
        this.websiteRecordRepository = websiteRecordRepository;
        this.threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    @Override
    public void crawl(WebsiteRecord record) {
        logger.info("Starting crawl for website record with ID: {}", record.getId());
        List<CrawledData> oldCrawledData = crawledDataRepository.findByOwner(record);
        ExecutionRecord execution = new ExecutionRecord();
        execution.setStatus(Status.IN_PROGRESS);
        execution.setStartTime(LocalDateTime.now());
        execution.setWebsiteRecord(record);
        execution.setNumberOfSitesCrawled(0);
        executionRecordRepository.save(execution);
        try {
            visitedUrls = Collections.newSetFromMap(new ConcurrentHashMap<>());
            urlsToCrawl = new ConcurrentLinkedQueue<>();
            AtomicInteger crawledCount = new AtomicInteger(0);
            urlsToCrawl.offer(record.getUrl());

            // Create worker tasks
            List<CompletableFuture<Void>> workerTasks = new ArrayList<>();
            for (int i = 0; i < MAX_CONCURRENT_TASKS; i++) {
                workerTasks.add(CompletableFuture.runAsync(() -> crawlWorker(record, crawledCount), threadPool));
            }

            // Wait for all worker tasks to complete
            CompletableFuture.allOf(workerTasks.toArray(new CompletableFuture[0])).join();

            execution.setStatus(Status.COMPLETED);
            record.setLastExecutionStatus(Status.COMPLETED);
            record.setLastExecutionTime(LocalDateTime.now());
            execution.setNumberOfSitesCrawled(crawledCount.get());

            // Delete old crawled data after successful execution
            crawledDataRepository.deleteAll(oldCrawledData);
            logger.info("Crawl completed for website record with ID: {}", record.getId());
        } catch (Exception e) {
            logger.error("Crawl failed for website record with ID: {}", record.getId(), e);
            execution.setStatus(Status.FAILED);
            record.setLastExecutionStatus(Status.FAILED);
        }
        execution.setEndTime(LocalDateTime.now());
        executionRecordRepository.save(execution);
        websiteRecordRepository.save(record);
    }

    private void crawlWorker(WebsiteRecord record, AtomicInteger crawledCount) {
        while (!urlsToCrawl.isEmpty()) {
            String url = urlsToCrawl.poll();
            if (url != null && visitedUrls.add(url)) {
                crawlPage(record, url, 0, crawledCount);
            }
        }
    }

    private void crawlPage(WebsiteRecord record, String url, int depth, AtomicInteger crawledCount) {
        if (depth >= MAX_DEPTH) {
            return;
        }

        crawledCount.incrementAndGet();
        try {
            Document doc = Jsoup.connect(url).get();

            CrawledData crawledData = new CrawledData();
            crawledData.setTitle(doc.title());
            crawledData.setUrl(url);
            crawledData.setCrawlTime(getDate());
            crawledData.setLinks(new ArrayList<>());
            crawledData.setOwner(record);

            Elements links = doc.select("a[href]");
            for (Element link : links) {
                String absUrl = link.attr("abs:href");
                if (isValidUrl(absUrl, record.getBoundaryRegexp()) && !visitedUrls.contains(absUrl)) {
                    crawledData.getLinks().add(absUrl);
                    urlsToCrawl.offer(absUrl);
                }
            }
            crawledDataRepository.save(crawledData);
        } catch (Exception e) {
            logger.error("Error crawling URL: {}", url, e.getMessage());
        }
    }

    /**
     * Checks if a given URL is valid and matches the boundary regular expression.
     *
     * @param url            the URL to check
     * @param boundaryRegExp the regular expression defining the boundaries of the crawl
     * @return {@code true} if the URL is valid and within the boundary, {@code false} otherwise
     */
    private boolean isValidUrl(String url, String boundaryRegExp) {

        // Check if the URL matches the boundary regular expression
        if (!Pattern.matches(boundaryRegExp, url)) {
            return false;
        }

        // Check for unsupported schemes
        for (String scheme : UNSUPPORTED_SCHEMES) {
            if (url.startsWith(scheme)) {
                return false;
            }
        }

        return true;
    }


    /**
     * Returns the current date and time as a formatted string.
     *
     * @return the current date and time in "yyyy-MM-dd HH:mm:ss" format
     */
    private String getDate() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
}
