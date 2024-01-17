package es.udc.ws.app.restservice.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import es.udc.ws.app.restservice.dto.RestMatchDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.InputStream;
import java.util.List;

public class JsonToRestMatchDtoConversor {

    public static ObjectNode toObjectNode(RestMatchDto match) {

        ObjectNode matchObject = JsonNodeFactory.instance.objectNode();

        matchObject.put("matchId", match.getIdPartido()).
                put("visitorTeam", match.getEquipoVisitante()).
                put("gameDate", match.getFechaJugar()).
                put("ticketPrice", match.getPrecioEntrada()).
                put("seats", match.getNumeroAsientos()).
                put("soldTickets", match.getEntradasVendidas());

        return matchObject;
    }

    public static ArrayNode toArrayNode(List<RestMatchDto> matches) {

        ArrayNode matchesNode = JsonNodeFactory.instance.arrayNode();
        for (int i = 0; i < matches.size(); i++) {
            RestMatchDto matchDto = matches.get(i);
            ObjectNode matchObject = toObjectNode(matchDto);
            matchesNode.add(matchObject);
        }

        return matchesNode;
    }

    public static RestMatchDto toRestMatchDto(InputStream jsonMatch) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonMatch);

            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                ObjectNode matchObject = (ObjectNode) rootNode;

                JsonNode matchIdNode = matchObject.get("matchId");
                Long matchId = (matchIdNode != null) ? matchIdNode.longValue() : null;

                String equipoVisitante = matchObject.get("visitorTeam").textValue().trim();
                String fechaJugar = matchObject.get("gameDate").textValue().trim();
                float precioEntrada = matchObject.get("ticketPrice").floatValue();
                int numeroAsientos = matchObject.get("seats").intValue();

                return new RestMatchDto(matchId, equipoVisitante, fechaJugar, precioEntrada,
                numeroAsientos, 0);
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }
}
