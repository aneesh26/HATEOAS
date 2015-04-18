package edu.asu.mscs.ashastry.appealserver.activities;

import edu.asu.mscs.ashastry.appealserver.model.Identifier;
import edu.asu.mscs.ashastry.appealserver.model.Order;
import edu.asu.mscs.ashastry.appealserver.repositories.OrderRepository;
import edu.asu.mscs.ashastry.appealserver.representations.OrderRepresentation;
import edu.asu.mscs.ashastry.appealserver.representations.RestbucksUri;

public class ReadOrderActivity {
    public OrderRepresentation retrieveByUri(RestbucksUri orderUri) {
        Identifier identifier  = orderUri.getId();
        
        Order order = OrderRepository.current().get(identifier);
        
        if(order == null) {
            throw new NoSuchOrderException();
        }
        
        return null;//OrderRepresentation.createResponseOrderRepresentation(order, orderUri);
    }
}
