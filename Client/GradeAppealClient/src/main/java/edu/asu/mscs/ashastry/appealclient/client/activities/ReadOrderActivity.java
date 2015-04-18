package edu.asu.mscs.ashastry.appealclient.client.activities;

import java.net.URI;

import edu.asu.mscs.ashastry.appealclient.client.ClientOrder;
import edu.asu.mscs.ashastry.appealclient.representations.OrderRepresentation;

public class ReadOrderActivity extends Activity {

    private final URI orderUri;
    private OrderRepresentation currentOrderRepresentation;

    public ReadOrderActivity(URI orderUri) {
        this.orderUri = orderUri;
    }

    public void readOrder() {
        try {
            currentOrderRepresentation = binding.retrieveOrder(orderUri);
            actions = new RepresentationHypermediaProcessor().extractNextActionsFromOrderRepresentation(currentOrderRepresentation);
        } catch (NotFoundException e) {
            actions = new Actions();
            actions.add(new PlaceOrderActivity());
        } catch (ServiceFailureException e) {
            actions = new Actions();
            actions.add(this);
        }
    }

    public ClientOrder getOrder() {
        return new ClientOrder(currentOrderRepresentation.getOrder());
    }
}
