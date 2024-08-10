package mff.webcrawler.controller;

import mff.webcrawler.model.WebsiteRecord;
import mff.webcrawler.repository.WebsiteRecordRepository;
import mff.webcrawler.service.Impl.WebCrawlerServiceImpl;
import mff.webcrawler.service.WebCrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class CrawlController {
    private final WebsiteRecordRepository websiteRecordRepository;

    private final WebCrawlerService webCrawlerService;

    @Autowired
    public CrawlController(WebsiteRecordRepository websiteRecordRepository, WebCrawlerService webCrawlerService){
        this.websiteRecordRepository = websiteRecordRepository;
        this.webCrawlerService = webCrawlerService;

    }

    @RequestMapping("/{id}")
    public ResponseEntity<Void> crawl(@PathVariable Long id){
        Optional<WebsiteRecord> record = websiteRecordRepository.findById(id);
        if(record.isPresent()){
            WebsiteRecord websiteRecord = record.get();
            webCrawlerService.crawl(websiteRecord);
            return  ResponseEntity.ok().build();
        }else {
            return  ResponseEntity.notFound().build();
        }
    }

}
