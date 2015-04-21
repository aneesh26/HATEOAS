package edu.asu.mscs.ashastry.appealserver.activities;

import edu.asu.mscs.ashastry.appealserver.model.Identifier;
import edu.asu.mscs.ashastry.appealserver.model.Order;
import edu.asu.mscs.ashastry.appealserver.model.OrderStatus;
import edu.asu.mscs.ashastry.appealserver.repositories.OrderRepository;
import edu.asu.mscs.ashastry.appealserver.representations.OrderRepresentation;

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
