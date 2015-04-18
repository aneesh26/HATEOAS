package edu.asu.mscs.ashastry.appealclient.activities;

import edu.asu.mscs.ashastry.appealclient.model.Identifier;
import edu.asu.mscs.ashastry.appealclient.model.Order;
import edu.asu.mscs.ashastry.appealclient.model.OrderStatus;
import edu.asu.mscs.ashastry.appealclient.repositories.OrderRepository;
import edu.asu.mscs.ashastry.appealclient.representations.OrderRepresentation;
import edu.asu.mscs.ashastry.appealclient.representations.RestbucksUri;

public class RemoveOrderActivity {
    public OrderRepresentation delete(RestbucksUri orderUri) {
        // Discover the URI of the order that has been cancelled
        
        Identifier identifier = orderUri.getId();

        OrderRepository orderRepository = OrderRepository.current();

        if (orderRepository.orderNotPlaced(identifier)) {
            throw new NoSuchOrderException();
        }

        Order order = orderRepository.get(identifier);

        // Can't delete a ready or preparing order
        if (order.getStatus() == OrderStatus.PREPARING || order.getStatus() == OrderStatus.READY) {
            throw new OrderDeletionException();
        }

        if(order.getStatus() == OrderStatus.UNPAID) { // An unpaid order is being cancelled 
            orderRepository.remove(identifier);
        }

        return new OrderRepresentation(order);
    }

}
