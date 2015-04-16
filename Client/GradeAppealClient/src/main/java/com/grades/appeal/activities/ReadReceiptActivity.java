package com.grades.appeal.activities;

import com.grades.appeal.model.Identifier;
import com.grades.appeal.model.OrderStatus;
import com.grades.appeal.model.Payment;
import com.grades.appeal.repositories.OrderRepository;
import com.grades.appeal.repositories.PaymentRepository;
import com.grades.appeal.representations.Link;
import com.grades.appeal.representations.ReceiptRepresentation;
import com.grades.appeal.representations.Representation;
import com.grades.appeal.representations.RestbucksUri;

public class ReadReceiptActivity {

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

}
