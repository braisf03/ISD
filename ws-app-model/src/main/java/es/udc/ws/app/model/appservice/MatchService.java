package es.udc.ws.app.model.appservice;

import es.udc.ws.app.model.appservice.exceptions.AdquiredAfterException;
import es.udc.ws.app.model.appservice.exceptions.AlreadyPickedException;
import es.udc.ws.app.model.appservice.exceptions.SalesLimitReachedException;
import es.udc.ws.app.model.appservice.exceptions.WrongCreditCardException;
import es.udc.ws.app.model.match.Match;
import es.udc.ws.app.model.ticket.Ticket;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.time.LocalDate;
import java.util.List;

public interface MatchService {

    public Match addMatch(Match match) throws InputValidationException;

    public Match findMatch(Long idPartido) throws InstanceNotFoundException;

    public List<Match> findByDates(LocalDate initDate, LocalDate endDate);

    public Ticket buyTicket(Long idPartido, String correoUsuario, String numTarjetaBancaria, int HowMany)
            throws InstanceNotFoundException, InputValidationException, SalesLimitReachedException, AdquiredAfterException;

    public List<Ticket> findByUser(String correoUsuario);

    public void giveTickets(Long idCompra,String numTarjetaBancaria)
            throws InstanceNotFoundException, AlreadyPickedException, WrongCreditCardException, InputValidationException;
}