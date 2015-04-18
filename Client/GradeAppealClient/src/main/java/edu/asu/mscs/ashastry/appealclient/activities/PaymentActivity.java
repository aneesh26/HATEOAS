package edu.asu.mscs.ashastry.appealclient.activities;

import edu.asu.mscs.ashastry.appealclient.model.Identifier;
import edu.asu.mscs.ashastry.appealclient.model.OrderStatus;
import edu.asu.mscs.ashastry.appealclient.model.Payment;
import edu.asu.mscs.ashastry.appealclient.repositories.OrderRepository;
import edu.asu.mscs.ashastry.appealclient.repositories.PaymentRepository;
import edu.asu.mscs.ashastry.appealclient.representations.Link;
//import edu.asu.mscs.ashastry.appealclient.representations.PaymentRepresentation;
import edu.asu.mscs.ashastry.appealclient.representations.Representation;
import edu.asu.mscs.ashastry.appealclient.representations.RestbucksUri;

public class PaymentActivity {
  
    /*
    public PaymentRepresentation pay(Payment payment, RestbucksUri paymentUri) {
        Identifier identifier = paymentUri.getId();
        
        // Don't know the order!
        if(!OrderRepository.current().has(identifier)) {
            throw new NoSuchOrderException();
        }
        
        // Already paid
        if(PaymentRepository.current().has(identifier)) {
            throw new UpdateException();
        }
        
        // Business rules - if the payment amount doesn't match the amount outstanding, then reject
        if(OrderRepository.current().get(identifier).calculateCost() != payment.getAmount()) {
            throw new InvalidPaymentException();
        }
        
        // If we get here, let's create the payment and update the order status
        OrderRepository.current().get(identifier).setStatus(OrderStatus.PREPARING);
        PaymentRepository.current().store(identifier, payment);
        
      //  return new PaymentRepresentation(payment, new Link(Representation.RELATIONS_URI + "order", UriExchange.orderForPayment(paymentUri)),
       //         new Link(Representation.RELATIONS_URI + "receipt", UriExchange.receiptForPayment(paymentUri)));
         return null;       
    }
    */
}
