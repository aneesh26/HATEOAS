package edu.asu.mscs.ashastry.appealserver.activities;

import edu.asu.mscs.ashastry.appealserver.model.Identifier;
import edu.asu.mscs.ashastry.appealserver.model.Order;
import edu.asu.mscs.ashastry.appealserver.model.OrderStatus;
import edu.asu.mscs.ashastry.appealserver.repositories.OrderRepository;
import edu.asu.mscs.ashastry.appealserver.representations.OrderRepresentation;
import edu.asu.mscs.ashastry.appealserver.representations.RestbucksUri;

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


        return null;//OrderRepresentation.createResponseOrderRepresentation(storedOrder, orderUri); 
    }
    
    private boolean orderCanBeChanged(Identifier identifier) {
        return OrderRepository.current().get(identifier).getStatus() == OrderStatus.UNPAID;
    }
}
