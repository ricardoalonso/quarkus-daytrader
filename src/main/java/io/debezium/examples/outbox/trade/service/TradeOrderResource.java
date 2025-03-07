package io.debezium.examples.outbox.trade.service;

import java.math.BigDecimal;
import java.util.Date;

import jakarta.inject.Inject;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;

import org.jboss.resteasy.reactive.RestStreamElementType;

import io.debezium.examples.outbox.trade.model.TradeOrder;
import io.smallrye.mutiny.Multi;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.core.eventbus.EventBus;

@Path("/")
public class TradeOrderResource {

    @Inject
    EventBus eventBus;

    @GET
    @Path("/stream")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @RestStreamElementType(MediaType.APPLICATION_JSON)
    public Multi<JsonObject> stream()
    {
        return eventBus.<JsonObject>consumer("order_stream")
                .toMulti()
                .map(b -> b.body());
    }

    @GET
    @Path("/try") 
    public String tryNow(){
        final long orderId = 110L;
        final String orderType = "BUY";
        final Date openDate = new Date();
        final String symbol = "E1";
        final int quantity = 500;
        final String price = new BigDecimal("19.99").toString();
        final String orderFee= new BigDecimal("5.00").toString();
        final int accountId = 12345;

        TradeOrder tradeOrder = new TradeOrder(orderId, orderType, openDate, symbol, quantity, price, orderFee, accountId);

        final JsonObject jsonObject = JsonObject.mapFrom(tradeOrder);
        eventBus.publish("order_stream", jsonObject);

        return "success";
    }
    
}