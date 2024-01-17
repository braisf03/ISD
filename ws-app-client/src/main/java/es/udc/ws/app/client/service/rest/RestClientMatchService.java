package es.udc.ws.app.client.service.rest;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.udc.ws.app.client.service.ClientMatchService;
import es.udc.ws.app.client.service.dto.ClientMatchDto;
import es.udc.ws.app.client.service.dto.ClientTicketDto;
import es.udc.ws.app.client.service.exceptions.ClientAdquiredAfterException;
import es.udc.ws.app.client.service.exceptions.ClientAlreadyPickedException;
import es.udc.ws.app.client.service.exceptions.ClientSalesLimitReachedException;
import es.udc.ws.app.client.service.exceptions.ClientWrongCreditCardException;
import es.udc.ws.app.client.service.rest.json.JsonToClientExceptionConversor;
import es.udc.ws.app.client.service.rest.json.JsonToClientMatchDtoConversor;
import es.udc.ws.app.client.service.rest.json.JsonToClientTicketDtoConversor;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.ObjectMapperFactory;
import org.apache.hc.client5.http.fluent.Form;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpStatus;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

public class RestClientMatchService implements ClientMatchService {
    private final static String ENDPOINT_ADDRESS_PARAMETER = "RestClientMatchService.endpointAddress";
    private String endpointAddress;

    @Override
    public Long addMatch(ClientMatchDto match) throws InputValidationException {

        try {
            InputStream matchIS = toInputStream(match);
            ContentType var = ContentType.create("application/json");
            String m = "matches";
            ClassicHttpResponse response = (ClassicHttpResponse) Request.post(getEndpointAddress() + m).
                    bodyStream(matchIS, var).execute().returnResponse();

            validateStatusCode(HttpStatus.SC_CREATED, response);

            return JsonToClientMatchDtoConversor.toClientMatchDto(response.getEntity().getContent()).getMatchId();

        } catch (InputValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    @Override
    public ClientMatchDto findMatch(Long idPartido) throws InstanceNotFoundException{
        try {

            ClassicHttpResponse response = (ClassicHttpResponse) Request.get(getEndpointAddress() + "matches/"
                            + idPartido).
                    execute().returnResponse();
            validateStatusCode(HttpStatus.SC_OK, response);

            return JsonToClientMatchDtoConversor.toClientMatchDto(response.getEntity()
                    .getContent());

        }catch (InstanceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<ClientMatchDto> findByDates(LocalDate startDate) {

        try {

            ClassicHttpResponse response = (ClassicHttpResponse) Request.get(getEndpointAddress() + "matches?endDate="
                            + URLEncoder.encode(String.valueOf(startDate), StandardCharsets.UTF_8)).
                    execute().returnResponse();
            validateStatusCode(HttpStatus.SC_OK, response);

            return JsonToClientMatchDtoConversor.toClientMatchDtos(response.getEntity()
                    .getContent());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Long buyTicket(Long idPartido, String correoUsuario, String numTarjetaBancaria, int HowMany)
            throws InstanceNotFoundException, InputValidationException, ClientSalesLimitReachedException, ClientAdquiredAfterException {

        try {

            ClassicHttpResponse response = (ClassicHttpResponse) Request.post(getEndpointAddress() + "tickets").
                    bodyForm(
                            Form.form().
                                    add("matchId", Long.toString(idPartido)).
                                    add("userEmail", correoUsuario).
                                    add("creditCardNumber", numTarjetaBancaria).
                                    add("ticketNumber",String.valueOf(HowMany)).
                                    add("ticketNumber",String.valueOf(HowMany)).
                                    build()).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_CREATED, response);

            //Hice una funcion en el JsonToClientTicketDtoConversor que se necesitaba para esta linea por
            //que nosotros en la que usabamos necesitabamos una lista y aqui necesita un ticket solo?

            return JsonToClientTicketDtoConversor.toClientTicketDto(
                    response.getEntity().getContent()).getIdCompra();

        } catch (InputValidationException | InstanceNotFoundException | ClientSalesLimitReachedException |ClientAdquiredAfterException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<ClientTicketDto> findByUser(String correoUsuario) {

        try {
            ClassicHttpResponse response = (ClassicHttpResponse) Request.get(getEndpointAddress() + "tickets?userEmail=" + correoUsuario).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_OK, response);

            return JsonToClientTicketDtoConversor.toClientTicketDtos(
                    response.getEntity().getContent());

        }catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void giveTickets(Long idCompra,String numTarjetaBancaria)
            throws InstanceNotFoundException, ClientAlreadyPickedException, ClientWrongCreditCardException, InputValidationException{
        try {

            ClassicHttpResponse response = (ClassicHttpResponse) Request.post(getEndpointAddress() + "tickets/" + idCompra + "/take").
                    bodyForm(
                            Form.form().
                                    add("creditCardNumber", numTarjetaBancaria).
                                    build()).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_NO_CONTENT, response);

        } catch (InputValidationException | InstanceNotFoundException | ClientWrongCreditCardException |ClientAlreadyPickedException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private synchronized String getEndpointAddress() {
        if (endpointAddress == null) {
            endpointAddress = ConfigurationParametersManager
                    .getParameter(ENDPOINT_ADDRESS_PARAMETER);
        }
        return endpointAddress;
    }

    private InputStream toInputStream(ClientMatchDto match) {

        try {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            objectMapper.writer(new DefaultPrettyPrinter()).writeValue(outputStream,
                    JsonToClientMatchDtoConversor.toObjectNode(match));

            return new ByteArrayInputStream(outputStream.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void validateStatusCode(int successCode, ClassicHttpResponse response) throws Exception {

        try {

            int statusCode = response.getCode();

            /* Success? */
            if (statusCode == successCode) {
                return;
            }

            /* Handler error. */
            switch (statusCode) {
                case HttpStatus.SC_NOT_FOUND -> throw JsonToClientExceptionConversor.fromNotFoundErrorCode(
                        response.getEntity().getContent());
                case HttpStatus.SC_BAD_REQUEST -> throw JsonToClientExceptionConversor.fromBadRequestErrorCode(
                        response.getEntity().getContent());
                case HttpStatus.SC_FORBIDDEN -> throw JsonToClientExceptionConversor.fromForbiddenErrorCode(
                        response.getEntity().getContent());
                case HttpStatus.SC_GONE -> throw JsonToClientExceptionConversor.fromGoneErrorCode(
                        response.getEntity().getContent());
                default -> throw new RuntimeException("HTTP error; status code = "
                        + statusCode);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
