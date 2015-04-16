package com.grades.appeal.activities;

import com.grades.appeal.model.Identifier;
import com.grades.appeal.model.Order;
import com.grades.appeal.model.OrderStatus;
import com.grades.appeal.repositories.OrderRepository;
import com.grades.appeal.representations.OrderRepresentation;

public class CompleteOrderActivity {

    public OrderRepresentation completeOrder(Identifier id) {
        OrderRepository repository = OrderRepository.current();
        if (repository.has(id)) {
            Order order = repository.get(id);

            if (order.getStatus() == OrderStatus.READY) {
                order.setStatus(OrderStatus.TAKEN);
            } else if (order.getStatus() == OrderStatus.TAKEN) {
                throw new OrderAlreadyCompletedException();
            } else if (order.getStatus() == OrderStatus.UNPAID) {
                throw new OrderNotPaidException();
            }

            return new OrderRepresentation(order);
        } else {
            throw new NoSuchOrderException();
        }
    }
}
