package mff.webcrawler.graphql;

import mff.webcrawler.model.CrawledData;
import mff.webcrawler.model.WebsiteRecord;
import mff.webcrawler.repository.CrawledDataRepository;
import mff.webcrawler.repository.WebsiteRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class QueryResolver{
    @Autowired
    WebsiteRecordRepository websiteRecordRepository;
    @Autowired
    CrawledDataRepository crawledDataRepository;

    @QueryMapping
    public List<WebsiteRecord> websites(){
        return websiteRecordRepository.findAll();
    }

    @QueryMapping
    public List<Node> nodes(@Argument("webPages") List<String> webPages) {
        return crawledDataRepository.findAllByOwnerIdIn(webPages.stream()
                        .map(Long::parseLong)
                        .collect(Collectors.toList()))
                .stream()
                .map(this::mapToNode)
                .collect(Collectors.toList());
    }
    private WebPage mapToWebPage(WebsiteRecord record) {
        return WebPage.builder()
                .id(record.getId().toString())
                .label(record.getLabel())
                .url(record.getUrl())
                .regexp(record.getBoundaryRegexp())
                .tags(record.getTags())
                .active(record.getActive())
                .build();
    }
    private Node mapToNode(CrawledData data) {
        return Node.builder()
                .title(data.getTitle())
                .url(data.getUrl())
                .crawlTime(data.getCrawlTime())
                .links(data.getLinks().stream()
                        .map(link -> Node.builder()
                                .url(link)
                                .links(Collections.emptyList())
                                .owner(mapToWebPage(data.getOwner()))
                                .build())
                        .collect(Collectors.toList()))
                .owner(mapToWebPage(data.getOwner()))
                .build();
    }
}
