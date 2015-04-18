package edu.asu.mscs.ashastry.appealclient.activities;

import edu.asu.mscs.ashastry.appealclient.model.Identifier;
import edu.asu.mscs.ashastry.appealclient.model.OrderStatus;
import edu.asu.mscs.ashastry.appealclient.model.Payment;
import edu.asu.mscs.ashastry.appealclient.repositories.OrderRepository;
import edu.asu.mscs.ashastry.appealclient.repositories.PaymentRepository;
import edu.asu.mscs.ashastry.appealclient.representations.Link;
//import edu.asu.mscs.ashastry.appealclient.representations.ReceiptRepresentation;
import edu.asu.mscs.ashastry.appealclient.representations.Representation;
import edu.asu.mscs.ashastry.appealclient.representations.RestbucksUri;

public class ReadReceiptActivity {
/*
    
   
    public ReceiptRepresentation read(RestbucksUri receiptUri) {
        Identifier identifier = receiptUri.getId();
        if(!orderHasBeenPaid(identifier)) {
            throw new OrderNotPaidException();
        } else if (OrderRepository.current().has(identifier) && OrderRepository.current().get(identifier).getStatus() == OrderStatus.TAKEN) {
            throw new OrderAlreadyCompletedException();
        }
        
        Payment payment = PaymentRepository.current().get(identifier);
        
        return new ReceiptRepresentation(payment, new Link(Representation.RELATIONS_URI + "order", UriExchange.orderForReceipt(receiptUri)));
    }

    private boolean orderHasBeenPaid(Identifier id) {
        return PaymentRepository.current().has(id);
    }
    
    */

}
