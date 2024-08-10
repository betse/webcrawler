package mff.webcrawler.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mff.webcrawler.Enums.Status;

import java.time.LocalDateTime;

/**
 * Entity representing the record of an execution of a web crawling process.
 * Each {@code ExecutionRecord} keeps track of a single run of the web crawler,
 * including details such as the start and end time, the status, and the number of sites crawled.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="execution_records")
public class ExecutionRecord {
    /**
     * The unique identifier for the execution record.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The {@link WebsiteRecord} entity that this execution record is associated with.
     */
    @ManyToOne
    @JoinColumn(name = "website_record_id")
    private WebsiteRecord websiteRecord;

    /**
     * The start time of the crawling execution.
     */
    private LocalDateTime startTime;

    /**
     * The End time of the crawling execution.
     */
    private LocalDateTime endTime;

    /**
     * The status of the crawling execution, which indicates whether the crawl
     * was completed successfully, failed, or is still in progress.
     */
    @Enumerated(EnumType.STRING)
    private Status status;

    /**
     * The number of sites that were crawled during this execution.
     */
    private int numberOfSitesCrawled;
}
