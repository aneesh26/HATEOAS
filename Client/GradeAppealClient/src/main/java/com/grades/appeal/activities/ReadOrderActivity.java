package com.grades.appeal.activities;

import com.grades.appeal.model.Identifier;
import com.grades.appeal.model.Order;
import com.grades.appeal.repositories.OrderRepository;
import com.grades.appeal.representations.OrderRepresentation;
import com.grades.appeal.representations.RestbucksUri;

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
