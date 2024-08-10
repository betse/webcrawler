package mff.webcrawler.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import mff.webcrawler.model.WebsiteRecord;
import mff.webcrawler.service.WebsiteRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/web_records")
@Tag(name = "Website Record Controller", description = "API for managing website records")
public class WebsiteRecordController {

    private final WebsiteRecordService websiteRecordService;

    public WebsiteRecordController(WebsiteRecordService websiteRecordService){
        this.websiteRecordService = websiteRecordService;
    }

    @GetMapping
    @Operation(summary = "Get all website records", description = "Retrieve all website records from the database.")
    public List<WebsiteRecord> getAllWebRecords(){
        return websiteRecordService.getAllWebRecords();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a website record by ID", description = "Retrieve a specific website record by its ID.")
    public ResponseEntity<WebsiteRecord> getRecordById(@PathVariable Long id){
        Optional<WebsiteRecord> record = websiteRecordService.getRecordById(id);
        return record.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new website record", description = "Create a new website record and save it to the database.")
    public WebsiteRecord createRecord(@RequestBody WebsiteRecord record) {
        return websiteRecordService.createRecord(record);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a website record", description = "Update an existing website record by its ID.")
    public ResponseEntity<WebsiteRecord> updateRecord(@PathVariable Long id, @RequestBody WebsiteRecord recordDetail) {
        Optional<WebsiteRecord> updateRecord = websiteRecordService.updateRecord(id, recordDetail);
        return updateRecord.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a website record", description = "Delete a website record by its ID.")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id){
        if(websiteRecordService.deleteRecord(id)){
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.notFound().build();
        }
    }

}
