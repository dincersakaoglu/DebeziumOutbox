package com.dangercodex.accountservice.config.debezium;

import io.debezium.config.Configuration;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class  DebeziumConnectorConfig {

    @Bean
    public Configuration configuration() {
        return Configuration.create()
                .with("name", "outbox-postgres") // Specifies the name of the Debezium connector.
                .with("database.server.name", "debezium_outbox") // Specifies the name of the database server to be used on the Kafka side.
                .with("database.hostname", "localhost") // Specifies the hostname of the PostgreSQL database.
                .with("database.port", "5432") // Specifies the connection port of the PostgreSQL database.
                .with("database.user", "postgres") // Specifies the username for the PostgreSQL database.
                .with("database.password", "postgres123") // Specifies the password for the PostgreSQL database user.
                .with("database.dbname", "debezium_outbox") // Specifies the name of the PostgreSQL database.
                .with("connector.class", "io.debezium.connector.postgresql.PostgresConnector") // Specifies the Debezium connector to be used for the PostgreSQL database.
                .with("skipped.operations", "t,d") // Specifies skipped operation types. // c for inserts/create, u for updates, d for deletes, t for truncates, and none to not skip any operations.
                .with("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore") // Specifies the type of storage for offset information.
                .with("offset.storage.file.filename", "offset.dat") // Specifies the path for the file where offset information is stored.
                .with("offset.flush.interval.ms", 60000) // Specifies how often offset information is saved (in milliseconds).
                .with("schema.history.internal", "io.debezium.storage.file.history.FileSchemaHistory")
                .with("schema.history.internal.file.filename", "schistory.dat")
                .with("topic.prefix", "test") // Specifies the prefix for Kafka topics.
                .with("decimal.handling.mode", "string") // Specifies the handling mode for decimal numbers.
                .with("wal_level", "logical") // Specifies the Write-Ahead Logging (WAL) level for PostgreSQL.
                .with("plugin.name", "pgoutput") // Specifies the name of the PostgreSQL plugin used by Debezium.
                .with("table.include.list", "public.outboxs") // Specifies the PostgreSQL table to be tracked.
                .with("tasks.max", "1") // Specifies the maximum number of concurrent tasks.
                .with("tombstones.on.delete", "false") // Specifies the handling of delete operations.
                .with("route.topic.regex", "") // Specifies a regex pattern for topic routing.
                .build();
    }
}
