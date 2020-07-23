/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.debezium.examples.outbox.trade.service;

import java.time.LocalDateTime;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.debezium.examples.outbox.trade.model.TradeOrder;

import com.fasterxml.jackson.databind.JsonNode;

@ApplicationScoped
public class TradeOrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TradeOrderService.class);

    @PersistenceContext
    EntityManager entityManager;

    @Transactional(value=TxType.MANDATORY)
    public void orderCreated(JsonNode event) {
        LOGGER.info("Processing 'OrderCreated' event: {}", event);

        final long orderId = event.get("id").asLong();
        final long customerId = event.get("accountId").asLong();
        final LocalDateTime orderDate = LocalDateTime.parse(event.get("openDate").asText());

        entityManager.persist(new TradeOrder(customerId, orderId, orderDate));
    }

    @Transactional(value=TxType.MANDATORY)
    public void orderLineUpdated(JsonNode event) {
        LOGGER.info("Processing 'OrderLineUpdated' event: {}", event);
    }
}