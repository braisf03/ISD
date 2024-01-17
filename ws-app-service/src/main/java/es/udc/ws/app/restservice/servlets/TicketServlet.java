package es.udc.ws.app.restservice.servlets;

import es.udc.ws.app.model.appservice.MatchServiceFactory;
import es.udc.ws.app.model.appservice.exceptions.AdquiredAfterException;
import es.udc.ws.app.model.appservice.exceptions.AlreadyPickedException;
import es.udc.ws.app.model.appservice.exceptions.SalesLimitReachedException;
import es.udc.ws.app.model.appservice.exceptions.WrongCreditCardException;
import es.udc.ws.app.model.ticket.Ticket;
import es.udc.ws.app.restservice.dto.RestTicketDto;
import es.udc.ws.app.restservice.dto.TicketToRestTicketDtoConversor;
import es.udc.ws.app.restservice.json.AppExceptionToJsonConversor;
import es.udc.ws.app.restservice.json.JsonToRestTicketDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.RestHttpServletTemplate;
import es.udc.ws.util.servlet.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TicketServlet extends RestHttpServletTemplate {

    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp) throws IOException,
            InputValidationException, InstanceNotFoundException {

        String var = req.getPathInfo();
        if(var == null){
            var = "";
        }

        var = ServletUtils.normalizePath(var);
        if(!var.equals("")){
            String creditCardNumber = ServletUtils.getMandatoryParameter(req, "creditCardNumber");

            if (!var.endsWith("/take")) {
                throw new InputValidationException("Invalid Request: invalid operation over Tickets");
            }
            String idAsString = var.substring(1, var.length() - 5);
            Long saleId;
            try {
                saleId = Long.valueOf(idAsString);
            } catch (NumberFormatException ex) {
                throw new InputValidationException("Invalid Request: invalid Ticket id '" + idAsString + "'");
            }

            try{
                MatchServiceFactory.getService().giveTickets(saleId, creditCardNumber);
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NO_CONTENT,
                        null, null);
            }
            catch(AlreadyPickedException ex){
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_GONE,
                        AppExceptionToJsonConversor.toAlreadyPickedException(ex),
                        null);
                return;
            }
            catch(WrongCreditCardException ex){
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN,
                        AppExceptionToJsonConversor.toWrongCreditCardException(ex),
                        null);
                return;
            }

        }
        else {

            Long matchId = ServletUtils.getMandatoryParameterAsLong(req, "matchId");
            String userEmail = ServletUtils.getMandatoryParameter(req, "userEmail");
            String creditCardNumber = ServletUtils.getMandatoryParameter(req, "creditCardNumber");
            Long ticketNumberLong = ServletUtils.getMandatoryParameterAsLong(req, "ticketNumber");
            int ticketNumber = ticketNumberLong != null? ticketNumberLong.intValue(): null;

            Ticket ticket;
            try {
                ticket = MatchServiceFactory.getService().buyTicket(matchId, userEmail, creditCardNumber, ticketNumber);
            }
            catch (SalesLimitReachedException ex) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN,
                        AppExceptionToJsonConversor.toSalesLimitReachedException(ex),
                        null);
                return;
            }
            catch (AdquiredAfterException ex) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_GONE,
                        AppExceptionToJsonConversor.toAdquiredAfterException(ex),
                        null);
                return;
            }
            RestTicketDto ticketDto = TicketToRestTicketDtoConversor.toRestTicketDto(ticket);
            String saleURL = ServletUtils.normalizePath(req.getRequestURL().toString()) + "/" + ticket.getIdCompra().toString();
            Map<String, String> headers = new HashMap<>(1);
            headers.put("Location", saleURL);
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED,
                    JsonToRestTicketDtoConversor.toObjectNode(ticketDto), headers);
        }
    }

    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp) throws IOException,
            InputValidationException {
        ServletUtils.checkEmptyPath(req);
        String userEmail = req.getParameter("userEmail");

        List<Ticket> tickets = MatchServiceFactory.getService().findByUser(userEmail);

        List<RestTicketDto> ticketDtos = TicketToRestTicketDtoConversor.toRestTicketDtos(tickets);
        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                JsonToRestTicketDtoConversor.toArrayNode(ticketDtos), null);
    }

}

