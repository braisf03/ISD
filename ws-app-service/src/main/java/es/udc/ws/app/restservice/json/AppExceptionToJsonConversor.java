package es.udc.ws.app.restservice.json;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.model.appservice.exceptions.AdquiredAfterException;
import es.udc.ws.app.model.appservice.exceptions.AlreadyPickedException;
import es.udc.ws.app.model.appservice.exceptions.SalesLimitReachedException;
import es.udc.ws.app.model.appservice.exceptions.WrongCreditCardException;

public class AppExceptionToJsonConversor {
    public static ObjectNode toAdquiredAfterException(AdquiredAfterException ex) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "AdquiredAfter");
        exceptionObject.put("matchId", (ex.getMatchId() != null) ? ex.getMatchId() : null);

        return exceptionObject;
    }

    public static ObjectNode toAlreadyPickedException(AlreadyPickedException ex) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "AlreadyPicked");
        exceptionObject.put("saleId", (ex.getIdCompra() != null) ? ex.getIdCompra() : null);

        return exceptionObject;
    }

    public static ObjectNode toSalesLimitReachedException(SalesLimitReachedException ex) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "SalesLimitReached");
        exceptionObject.put("matchId", (ex.getMatchId() != null) ? ex.getMatchId() : null);

        return exceptionObject;
    }

    public static ObjectNode toWrongCreditCardException(WrongCreditCardException ex) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "WrongCreditCard");
        exceptionObject.put("saleId", (ex.getIdCompra() != null) ? ex.getIdCompra() : null);
        exceptionObject.put("creditCardNumber", (ex.getCreditCardNumber() != null) ? ex.getCreditCardNumber() : null);

        return exceptionObject;
    }
}
