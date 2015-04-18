package edu.asu.mscs.ashastry.appealclient.activities;

import edu.asu.mscs.ashastry.appealclient.model.Identifier;
import edu.asu.mscs.ashastry.appealclient.model.Order;
import edu.asu.mscs.ashastry.appealclient.model.OrderStatus;
import edu.asu.mscs.ashastry.appealclient.repositories.OrderRepository;
import edu.asu.mscs.ashastry.appealclient.representations.Link;
import edu.asu.mscs.ashastry.appealclient.representations.OrderRepresentation;
import edu.asu.mscs.ashastry.appealclient.representations.Representation;
import edu.asu.mscs.ashastry.appealclient.representations.RestbucksUri;

public class CreateOrderActivity {
    public OrderRepresentation create(Order order, RestbucksUri requestUri) {
        order.setStatus(OrderStatus.UNPAID);
                
        Identifier identifier = OrderRepository.current().store(order);
        
        RestbucksUri orderUri = new RestbucksUri(requestUri.getBaseUri() + "/order/" + identifier.toString());
        RestbucksUri paymentUri = new RestbucksUri(requestUri.getBaseUri() + "/payment/" + identifier.toString());
     /*   return new OrderRepresentation(order, 
                new Link(Representation.RELATIONS_URI + "cancel", orderUri), 
                new Link(Representation.RELATIONS_URI + "payment", paymentUri), 
                new Link(Representation.RELATIONS_URI + "update", orderUri),
                new Link(Representation.SELF_REL_VALUE, orderUri));
             */
        return null;
    }
}
