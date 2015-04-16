package com.grade.appeal.activities;

import com.grade.appeal.model.Identifier;
import com.grade.appeal.model.Order;
import com.grade.appeal.model.OrderStatus;
import com.grade.appeal.repositories.OrderRepository;
import com.grade.appeal.representations.OrderRepresentation;
import com.grade.appeal.representations.RestbucksUri;

public class UpdateOrderActivity {
    public OrderRepresentation update(Order order, RestbucksUri orderUri) {
        Identifier orderIdentifier = orderUri.getId();

        OrderRepository repository = OrderRepository.current();
        if (OrderRepository.current().orderNotPlaced(orderIdentifier)) { // Defensive check to see if we have the order
            throw new NoSuchOrderException();
        }

        if (!orderCanBeChanged(orderIdentifier)) {
            throw new UpdateException();
        }

        Order storedOrder = repository.get(orderIdentifier);
        
        storedOrder.setStatus(storedOrder.getStatus());
        storedOrder.calculateCost();


        return OrderRepresentation.createResponseOrderRepresentation(storedOrder, orderUri); 
    }
    
    private boolean orderCanBeChanged(Identifier identifier) {
        return OrderRepository.current().get(identifier).getStatus() == OrderStatus.UNPAID;
    }
}
