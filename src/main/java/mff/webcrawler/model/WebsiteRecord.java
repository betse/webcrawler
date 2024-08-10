package mff.webcrawler.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import mff.webcrawler.Enums.Periodicity;
import mff.webcrawler.Enums.Status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Represents a record of a website that is being crawled.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "website_records")
public class WebsiteRecord {
    /**
     * The unique identifier for the website record.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    /**
     * The URL of the website to be crawled.
     */
    private String url;

    /**
     * The regular expression that defines the boundaries of the crawl.
     */
    private String boundaryRegexp;

    /**
     * A label for the website record.
     */
    private String label;

    /**
     * Indicates whether the crawling for this website is active.
     */
    private Boolean active;

    /**
     * A set of tags associated with the website.
     */
    private Set<String> tags;

    /**
     * The periodicity of the crawling process.
     */
    @Enumerated(EnumType.STRING)
    private Periodicity periodicity;

    /**
     * The last time the crawling process was executed.
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime lastExecutionTime;

    /**
     * The status of the last execution.
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Enumerated(EnumType.STRING)
    private Status lastExecutionStatus;

    /**
     * The list of execution records associated with this website.
     */
    @Schema(hidden = true)
    @OneToMany(mappedBy = "websiteRecord", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExecutionRecord> executions;

    /**
     * The list of crawled data associated with this website.
     */
    @Schema(hidden = true)
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CrawledData> crawledData = new ArrayList<>();
}
