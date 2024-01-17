package es.udc.ws.app.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.node.JsonNodeType;

import es.udc.ws.app.client.service.exceptions.ClientAdquiredAfterException;
import es.udc.ws.app.client.service.exceptions.ClientAlreadyPickedException;
import es.udc.ws.app.client.service.exceptions.ClientSalesLimitReachedException;
import es.udc.ws.app.client.service.exceptions.ClientWrongCreditCardException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.InputStream;


public class JsonToClientExceptionConversor {
    public static Exception fromBadRequestErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                if (errorType.equals("InputValidation")) {
                    return toInputValidationException(rootNode);
                } else {
                    throw new ParsingException("Unrecognized error type: " + errorType);
                }
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static InputValidationException toInputValidationException(JsonNode rootNode) {
        String message = rootNode.get("message").textValue();
        return new InputValidationException(message);
    }

    public static Exception fromNotFoundErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                if (errorType.equals("InstanceNotFound")) {
                    return toInstanceNotFoundException(rootNode);
                } else {
                    throw new ParsingException("Unrecognized error type: " + errorType);
                }
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static InstanceNotFoundException toInstanceNotFoundException(JsonNode rootNode) {
        String instanceId = rootNode.get("instanceId").textValue();
        String instanceType = rootNode.get("instanceType").textValue();
        return new InstanceNotFoundException(instanceId, instanceType);
    }

    public static Exception fromForbiddenErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                if (errorType.equals("WrongCreditCard")) {
                    return toWrongCreditCardException(rootNode);
                } else if (errorType.equals("SalesLimitReached")) {
                    return toSalesLimitReachedException(rootNode);
                } else {
                    throw new ParsingException("Unrecognized error type: " + errorType);
                }
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }

    }
    private static ClientWrongCreditCardException toWrongCreditCardException(JsonNode rootNode) {
        Long matchId = rootNode.get("saleId").longValue();
        String creditCardNumber = rootNode.get("creditCardNumber").textValue();
        return new ClientWrongCreditCardException(matchId, creditCardNumber);
    }

    private static ClientSalesLimitReachedException toSalesLimitReachedException(JsonNode rootNode) {
        Long matchId = rootNode.get("matchId").longValue();
        return new ClientSalesLimitReachedException(matchId);
    }

    public static Exception fromGoneErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                if (errorType.equals("AlreadyPicked")) {
                    return toAlreadyPickedException(rootNode);
                } else if (errorType.equals("AdquiredAfter")) {
                    return toAdquiredAfterException(rootNode);
                }  else {
                    throw new ParsingException("Unrecognized error type: " + errorType);
                }
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }
    private static ClientAlreadyPickedException toAlreadyPickedException(JsonNode rootNode) {
        Long saleId = rootNode.get("saleId").longValue();

        return new ClientAlreadyPickedException(saleId);
    }

    private static ClientAdquiredAfterException toAdquiredAfterException(JsonNode rootNode) {
        Long matchId = rootNode.get("matchId").longValue();

        return new ClientAdquiredAfterException(matchId);
    }
}
