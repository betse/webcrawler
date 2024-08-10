package mff.webcrawler.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing the data that has been crawled from a website.
 * Each {@code CrawledData} instance stores information about a specific webpage,
 * including its title, URL, the time it was crawled, and the links found on the page.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="nodes")
public class CrawledData {

    /**
     * The unique identifier for the crawled data entry.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The title of the webpage that was crawled.
     */
    private String title;

    /**
     * The URL of the webpage that was crawled.
     */
    private String url;

    /**
     * The time at which the webpage was crawled, stored as a string.
     */
    private String crawlTime;

    /**
     * The {@link WebsiteRecord} entity that owns this crawled data.
     * This indicates which website record this data is associated with.
     */
    @ManyToOne
    @JoinColumn(name = "website_record_id")
    private WebsiteRecord owner;

    /**
     * A list of URLs found on the crawled webpage.
     */
    @ElementCollection
    private List<String> links = new ArrayList<>();
}
