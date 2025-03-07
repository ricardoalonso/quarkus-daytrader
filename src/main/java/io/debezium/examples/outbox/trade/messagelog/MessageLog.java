/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.debezium.examples.outbox.trade.messagelog;

import java.time.Instant;
import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class MessageLog {
    private static final Logger LOG = LoggerFactory.getLogger(MessageLog.class);

    @PersistenceContext
    EntityManager entityManager;

    @Transactional(value=TxType.MANDATORY)
    public void processed(UUID eventId) {
        entityManager.persist(new ConsumedMessage(eventId, Instant.now()));
    }

    @Transactional(value=TxType.MANDATORY)
    public boolean alreadyProcessed(UUID eventId) {
        LOG.debug("Looking for event with id {} in message log", eventId);
        return entityManager.find(ConsumedMessage.class, eventId) != null;
    }
}
