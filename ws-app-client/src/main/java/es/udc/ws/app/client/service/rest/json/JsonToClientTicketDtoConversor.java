package es.udc.ws.app.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.client.service.dto.ClientTicketDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JsonToClientTicketDtoConversor {
    public static ClientTicketDto toClientTicketDto2(JsonNode rootNode) throws ParsingException {
        ObjectNode ticketObject = (ObjectNode) rootNode;

        JsonNode ticketIdNode = ticketObject.get("saleId");
        Long ticketId = (ticketIdNode != null) ? ticketIdNode.longValue() : null;

        Long matchId = ticketObject.get("matchId").longValue();
        String correoUsuario = ticketObject.get("userEmail").textValue().trim();
        String numTarjetaBancaria = ticketObject.get("creditCardNumber").textValue().trim();
        String fechaHoraCompra = ticketObject.get("saleDate").textValue().trim();
        int numEntradas = ticketObject.get("ticketNumber").intValue();
        String recogidas = ticketObject.get("taken").intValue() == 1? "Sí": "No";

        return new ClientTicketDto(ticketId, correoUsuario, numTarjetaBancaria,
                LocalDateTime.parse(fechaHoraCompra), matchId, numEntradas, recogidas);

    }

    public static ClientTicketDto toClientTicketDto(InputStream jsonSale) throws ParsingException {

        try {

            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonSale);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                ObjectNode ticketObject = (ObjectNode) rootNode;

                JsonNode ticketIdNode = ticketObject.get("saleId");
                Long ticketId = (ticketIdNode != null) ? ticketIdNode.longValue() : null;

                Long matchId = ticketObject.get("matchId").longValue();
                String correoUsuario = ticketObject.get("userEmail").textValue().trim();
                String numTarjetaBancaria = ticketObject.get("creditCardNumber").textValue().trim();
                String fechaHoraCompra = ticketObject.get("saleDate").textValue().trim();
                int numEntradas = ticketObject.get("ticketNumber").intValue();
                String recogidas = ticketObject.get("taken").intValue() == 1? "Sí": "No";
                return new ClientTicketDto(ticketId, correoUsuario, numTarjetaBancaria,
                        LocalDateTime.parse(fechaHoraCompra), matchId, numEntradas, recogidas);

            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }


    }

    public static List<ClientTicketDto> toClientTicketDtos(InputStream jsonTickets) throws ParsingException {
        try {

            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonTickets);
            if (rootNode.getNodeType() != JsonNodeType.ARRAY) {
                throw new ParsingException("Unrecognized JSON (array expected)");
            } else {
                ArrayNode ticketsArray = (ArrayNode) rootNode;
                List<ClientTicketDto> ticketDtos = new ArrayList<>(ticketsArray.size());
                for (JsonNode ticketNode : ticketsArray) {
                    ticketDtos.add(toClientTicketDto2(ticketNode));
                }

                return ticketDtos;
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

}
