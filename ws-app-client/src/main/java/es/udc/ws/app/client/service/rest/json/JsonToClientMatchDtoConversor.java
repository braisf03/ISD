package es.udc.ws.app.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.client.service.dto.ClientMatchDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JsonToClientMatchDtoConversor {
    public static ObjectNode toObjectNode(ClientMatchDto match) {

        ObjectNode matchObject = JsonNodeFactory.instance.objectNode();

        if (match.getMatchId() != null) {
            matchObject.put("matchId", match.getMatchId());
        }
        matchObject.put("visitorTeam", match.getEquipoVisitante()).
                put("gameDate", match.getFechaJugar().toString()).
                put("ticketPrice", match.getPrecioEntrada()).
                put("seats", match.getNumeroAsientos());

        return matchObject;
    }

    public static ClientMatchDto toClientMatchDto(InputStream jsonMatch) throws ParsingException {
        try {

            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonMatch);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                return toClientMatchDto(rootNode);
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public static List<ClientMatchDto> toClientMatchDtos(InputStream jsonMatches) throws ParsingException {
        try {

            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonMatches);
            if (rootNode.getNodeType() != JsonNodeType.ARRAY) {
                throw new ParsingException("Unrecognized JSON (array expected)");
            } else {
                ArrayNode matchArray = (ArrayNode) rootNode;
                List<ClientMatchDto> matchDtos = new ArrayList<>(matchArray.size());
                for (JsonNode matchNode : matchArray) {
                    matchDtos.add(toClientMatchDto(matchNode));
                }

                return matchDtos;
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static ClientMatchDto toClientMatchDto(JsonNode matchNode) throws ParsingException {
        if (matchNode.getNodeType() != JsonNodeType.OBJECT) {
            throw new ParsingException("Unrecognized JSON (object expected)");
        } else {
            ObjectNode matchObject = (ObjectNode) matchNode;

            JsonNode matchIdNode = matchObject.get("matchId");

            Long matchId = (matchIdNode != null) ? matchIdNode.longValue() : null;
            String equipoVisitante = matchObject.get("visitorTeam").textValue().trim();
            LocalDateTime fechaJugar = LocalDateTime.parse(matchObject.get("gameDate").textValue().trim());
            float precioEntrada = matchObject.get("ticketPrice").floatValue();
            int numeroAsientos = matchObject.get("seats").intValue();
            int entradasRestantes = numeroAsientos - matchObject.get("soldTickets").intValue();


            return new ClientMatchDto(matchId, equipoVisitante, fechaJugar, precioEntrada, numeroAsientos,
                    entradasRestantes);
        }
    }
}
