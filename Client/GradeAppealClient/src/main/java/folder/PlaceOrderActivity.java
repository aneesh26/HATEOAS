package edu.asu.mscs.ashastry.appealclient.client.activities;

import java.net.URI;

import edu.asu.mscs.ashastry.appealclient.client.ClientOrder;
import edu.asu.mscs.ashastry.appealclient.model.Order;
import edu.asu.mscs.ashastry.appealclient.representations.OrderRepresentation;

public class PlaceOrderActivity extends Activity {

    private Order order;

    public void placeOrder(Order order, URI orderingUri) {
        
        try {
            OrderRepresentation createdOrderRepresentation = binding.createOrder(order, orderingUri);
            this.actions = new RepresentationHypermediaProcessor().extractNextActionsFromOrderRepresentation(createdOrderRepresentation);
            this.order = createdOrderRepresentation.getOrder();
        } catch (MalformedOrderException e) {
            this.actions = retryCurrentActivity();
        } catch (ServiceFailureException e) {
            this.actions = retryCurrentActivity();
        }
    }
    
    public ClientOrder getOrder() {
        return new ClientOrder(order);
    }
}
