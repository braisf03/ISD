package es.udc.ws.app.client.service;

import es.udc.ws.app.client.service.dto.ClientMatchDto;
import es.udc.ws.app.client.service.dto.ClientTicketDto;
import es.udc.ws.app.client.service.exceptions.ClientAdquiredAfterException;
import es.udc.ws.app.client.service.exceptions.ClientAlreadyPickedException;
import es.udc.ws.app.client.service.exceptions.ClientSalesLimitReachedException;
import es.udc.ws.app.client.service.exceptions.ClientWrongCreditCardException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.time.LocalDate;
import java.util.List;

public interface ClientMatchService {
    public Long addMatch(ClientMatchDto match) throws InputValidationException;

    public ClientMatchDto findMatch(Long idPartido) throws InstanceNotFoundException;

    public List<ClientMatchDto> findByDates(LocalDate endDate);

    public Long buyTicket(Long idPartido, String correoUsuario, String numTarjetaBancaria, int HowMany)
            throws InstanceNotFoundException, InputValidationException, ClientSalesLimitReachedException, ClientAdquiredAfterException;

    public List<ClientTicketDto> findByUser(String correoUsuario);

    public void giveTickets(Long idCompra,String numTarjetaBancaria)
            throws InstanceNotFoundException, ClientAlreadyPickedException, ClientWrongCreditCardException, InputValidationException;
}
