package mff.webcrawler.graphql;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class Node {
    private String title;
    private String url;
    private String crawlTime;
    private List<Node> links;
    private WebPage owner;
}