package com.dangercodex.mailservice.consumer;

import com.dangercodex.mailservice.model.KafkaPayload;
import com.dangercodex.mailservice.service.MailService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer {
    private static final String TOPIC_NAME = "account-created";
    private static final String GROUP_ID = "GroupId";
    private static final String CONTAINER_FACTORY = "ContainerFactory";

    private final MailService mailService;
    private final ObjectMapper MAPPER = new ObjectMapper();

    @KafkaListener(topics = {TOPIC_NAME}, groupId = GROUP_ID, containerFactory = CONTAINER_FACTORY)
    public void listener(@Payload Object event, ConsumerRecord c) throws Exception {
        // Retrieve the value of the Kafka record as a string
        String value = (String) c.value();

        // Convert the value string to a JSON node
        JsonNode payload = MAPPER.readTree(value);
        log.info("JSON NODE: {}", payload);

        // Convert from JSON to KafkaPayload object
        KafkaPayload kafkaPayload = MAPPER.readValue(payload.get("payload").asText(), KafkaPayload.class);
        log.info("KafkaPayload: {}", kafkaPayload);
        log.info("KafkaPayload getId: {}", kafkaPayload.getId());

        // Send an email using the email service
        mailService.sendMail(kafkaPayload.getUsername());

        // Delete a specific process from the Outbox
        mailService.deleteProcessByIdFromOutbox(kafkaPayload.getId());
    }
}

