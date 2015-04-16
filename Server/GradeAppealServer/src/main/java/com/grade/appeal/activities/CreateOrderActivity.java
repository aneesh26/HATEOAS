package com.grade.appeal.activities;

import com.grade.appeal.model.Identifier;
import com.grade.appeal.model.Order;
import com.grade.appeal.model.OrderStatus;
import com.grade.appeal.repositories.OrderRepository;
import com.grade.appeal.representations.Link;
import com.grade.appeal.representations.OrderRepresentation;
import com.grade.appeal.representations.Representation;
import com.grade.appeal.representations.RestbucksUri;

public class CreateOrderActivity {
    public OrderRepresentation create(Order order, RestbucksUri requestUri) {
        order.setStatus(OrderStatus.UNPAID);
                
        Identifier identifier = OrderRepository.current().store(order);
        
        RestbucksUri orderUri = new RestbucksUri(requestUri.getBaseUri() + "/order/" + identifier.toString());
        RestbucksUri paymentUri = new RestbucksUri(requestUri.getBaseUri() + "/payment/" + identifier.toString());
        return new OrderRepresentation(order, 
                new Link(Representation.RELATIONS_URI + "cancel", orderUri), 
                new Link(Representation.RELATIONS_URI + "payment", paymentUri), 
                new Link(Representation.RELATIONS_URI + "update", orderUri),
                new Link(Representation.SELF_REL_VALUE, orderUri));
    }
}
