package com.grades.appeal.client.activities;

import java.net.URI;

import com.grades.appeal.client.ClientOrder;
import com.grades.appeal.model.Order;
import com.grades.appeal.representations.OrderRepresentation;

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
