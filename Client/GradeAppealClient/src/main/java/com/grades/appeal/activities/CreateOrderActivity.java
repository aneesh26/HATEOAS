package com.grades.appeal.activities;

import com.grades.appeal.model.Identifier;
import com.grades.appeal.model.Order;
import com.grades.appeal.model.OrderStatus;
import com.grades.appeal.repositories.OrderRepository;
import com.grades.appeal.representations.Link;
import com.grades.appeal.representations.OrderRepresentation;
import com.grades.appeal.representations.Representation;
import com.grades.appeal.representations.RestbucksUri;

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
