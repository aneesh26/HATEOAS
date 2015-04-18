package edu.asu.mscs.ashastry.appealserver.activities;

import edu.asu.mscs.ashastry.appealserver.representations.AppealServerUri;

public class UriExchange {
/*
    public static AppealServerUri paymentForOrder(AppealServerUri orderUri) {
        checkForValidOrderUri(orderUri);
        return new AppealServerUri(orderUri.getBaseUri() + "/payment/" + orderUri.getId().toString());
    }
    
    public static AppealServerUri orderForPayment(RestbucksUri paymentUri) {
        checkForValidPaymentUri(paymentUri);
        return new RestbucksUri(paymentUri.getBaseUri() + "/order/" + paymentUri.getId().toString());
    }

    public static RestbucksUri receiptForPayment(RestbucksUri paymentUri) {
        checkForValidPaymentUri(paymentUri);
        return new RestbucksUri(paymentUri.getBaseUri() + "/receipt/" + paymentUri.getId().toString());
    }
    
    public static RestbucksUri orderForReceipt(RestbucksUri receiptUri) {
        checkForValidReceiptUri(receiptUri);
        return new RestbucksUri(receiptUri.getBaseUri() + "/order/" + receiptUri.getId().toString());
    }
*/
    private static void checkForValidWithdrawUri(AppealServerUri orderUri) {
        if(!orderUri.toString().contains("/withdraw")) {
            throw new RuntimeException("Invalid appeal URI");
        }
    }
  /*  
    private static void checkForValidPaymentUri(RestbucksUri payment) {
        if(!payment.toString().contains("/payment/")) {
            throw new RuntimeException("Invalid Payment URI");
        }
    }
    
    private static void checkForValidReceiptUri(RestbucksUri receipt) {
        if(!receipt.toString().contains("/receipt/")) {
            throw new RuntimeException("Invalid Receipt URI");
        }
    }
    */
}
