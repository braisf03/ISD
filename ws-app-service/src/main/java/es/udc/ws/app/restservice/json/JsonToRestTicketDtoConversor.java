package es.udc.ws.app.restservice.json;


import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import es.udc.ws.app.restservice.dto.RestTicketDto;


import java.io.InputStream;
import java.util.List;

public class JsonToRestTicketDtoConversor {
    public static ObjectNode toObjectNode(RestTicketDto ticket) {

        ObjectNode ticketObject = JsonNodeFactory.instance.objectNode();

        ticketObject.put("saleId", ticket.getIdCompra()).
                put("matchId", ticket.getIdPartido()).
                put("userEmail", ticket.getCorreoUsuario()).
                put("saleDate", ticket.getFechaHoraCompra()).
                put("creditCardNumber", ticket.getNumTarjetaBancaria()).
                put("ticketNumber", ticket.getNumEntradas()).
                put("taken", ticket.getRecogidas());

        return ticketObject;
    }

    public static ArrayNode toArrayNode(List<RestTicketDto> tickets) {

        ArrayNode ticketsNode = JsonNodeFactory.instance.arrayNode();
        for (int i = 0; i < tickets.size(); i++) {
            RestTicketDto ticketDto = tickets.get(i);
            ObjectNode ticketObject = toObjectNode(ticketDto);
            ticketsNode.add(ticketObject);
        }

        return ticketsNode;
    }


}
