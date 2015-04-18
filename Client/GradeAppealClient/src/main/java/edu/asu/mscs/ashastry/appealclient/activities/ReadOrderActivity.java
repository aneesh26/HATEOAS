package edu.asu.mscs.ashastry.appealclient.activities;

import edu.asu.mscs.ashastry.appealclient.model.Identifier;
import edu.asu.mscs.ashastry.appealclient.model.Order;
import edu.asu.mscs.ashastry.appealclient.repositories.OrderRepository;
import edu.asu.mscs.ashastry.appealclient.representations.OrderRepresentation;
import edu.asu.mscs.ashastry.appealclient.representations.RestbucksUri;

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
