package mff.webcrawler.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import mff.webcrawler.model.ExecutionRecord;
import mff.webcrawler.service.ExecutionRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/execution_records")
@Tag(name = "Execution Record Controller", description = "API for managing execution records")
public class ExecutionRecordController {

    private final ExecutionRecordService executionRecordService;

    @Autowired
    public ExecutionRecordController(ExecutionRecordService executionRecordService){
        this.executionRecordService = executionRecordService;
    }

    @GetMapping
    @Operation(summary = "Get all execution records", description = "Retrieve all execution records from the database.")
    public List<ExecutionRecord> getAllRecords(){
        return executionRecordService.getAllExecutionRecords();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an execution record by ID", description = "Retrieve a specific execution record by its ID.")
    public ResponseEntity<ExecutionRecord> getRecordById(@PathVariable Long id){
        Optional<ExecutionRecord> record =  executionRecordService.findById(id);
        return record.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an execution record", description = "Delete an execution record by its ID.")
    public ResponseEntity<Void> deleteRecords(@PathVariable Long id){
        if(executionRecordService.deleteRecord(id)){
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.notFound().build();
        }
    }


}
