package mff.webcrawler.graphql;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class WebPage {
    private String id;
    private String label;
    private String url;
    private String regexp;
    private Set<String> tags;
    private boolean active;
}
