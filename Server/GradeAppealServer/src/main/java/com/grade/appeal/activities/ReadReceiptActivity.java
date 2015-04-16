package com.grade.appeal.activities;

import com.grade.appeal.model.Identifier;
import com.grade.appeal.model.OrderStatus;
import com.grade.appeal.model.Payment;
import com.grade.appeal.repositories.OrderRepository;
import com.grade.appeal.repositories.PaymentRepository;
import com.grade.appeal.representations.Link;
import com.grade.appeal.representations.ReceiptRepresentation;
import com.grade.appeal.representations.Representation;
import com.grade.appeal.representations.RestbucksUri;

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
