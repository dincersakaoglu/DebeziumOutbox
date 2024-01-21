package com.dangercodex.accountservice.util;

import com.dangercodex.accountservice.service.OutboxService;
import io.debezium.config.Configuration;
import io.debezium.embedded.EmbeddedEngine;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static io.debezium.data.Envelope.FieldName.AFTER;
import static io.debezium.data.Envelope.FieldName.OPERATION;

@Slf4j
@Component
public class DebeziumUtil {
    // This class encapsulates the usage of Debezium, a change data capture tool.

    // This Executor manages and executes background tasks.
    private final Executor executor = Executors.newSingleThreadExecutor(); // An Executor is created to manage a background task.
    private final EmbeddedEngine engine; // Debezium EmbeddedEngine is used for capturing and processing changes.
    private final OutboxService outboxService;

    @PostConstruct
    public void start() {
        executor.execute(engine); // Executor is used for asynchronous processing of database changes.
    }

    @PreDestroy
    public void stop() {
        if (engine != null) {
            engine.stop(); // Properly shuts down the Debezium engine when the application is terminated.
        }
    }

    public DebeziumUtil(Configuration configuration, OutboxService outboxService) {
        this.engine = EmbeddedEngine.create()
                .using(configuration)
                .notifying(this::handleEvent)
                .build();
        this.outboxService = outboxService;
    }

    // SourceRecord is a class used in change data capture tools like Debezium for tracking and capturing certain data changes.
    private void handleEvent(SourceRecord sourceRecord) {
        // Process the change data and save it appropriately.
        Struct sourceRecordValue = (Struct) sourceRecord.value(); // Extracts the value from the sourceRecord.

        // Extract the CRUD operation from the source to determine the type of change (create, update, delete, etc.).
        var crudOperation = (String) sourceRecordValue.get(OPERATION);

        // CRUD operations are checked, and changes are processed accordingly.
        if (sourceRecordValue != null && (crudOperation == "c" || crudOperation == "u")) {
            Struct struct = (Struct) sourceRecordValue.get(AFTER);
            Map<String, Object> payload = struct.schema().fields().stream()
                    .filter(field -> struct.get(field) != null)
                    .collect(Collectors.toMap(Field::name, field -> struct.get(field))); // Processing and saving the change data.

            outboxService.debeziumDatabaseChange(payload); // Relevant service is used for the business logic processing.
        }
    }
}

