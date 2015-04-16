package com.grade.appeal.activities;

import com.grade.appeal.model.Identifier;
import com.grade.appeal.model.Order;
import com.grade.appeal.repositories.OrderRepository;
import com.grade.appeal.representations.OrderRepresentation;
import com.grade.appeal.representations.RestbucksUri;

public class ReadOrderActivity {
    public OrderRepresentation retrieveByUri(RestbucksUri orderUri) {
        Identifier identifier  = orderUri.getId();
        
        Order order = OrderRepository.current().get(identifier);
        
        if(order == null) {
            throw new NoSuchOrderException();
        }
        
        return OrderRepresentation.createResponseOrderRepresentation(order, orderUri);
    }
}
