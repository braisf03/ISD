package es.udc.ws.app.thriftservice;

import es.udc.ws.app.model.appservice.MatchServiceFactory;
import es.udc.ws.app.model.appservice.exceptions.AdquiredAfterException;
import es.udc.ws.app.model.appservice.exceptions.AlreadyPickedException;
import es.udc.ws.app.model.appservice.exceptions.SalesLimitReachedException;
import es.udc.ws.app.model.appservice.exceptions.WrongCreditCardException;
import es.udc.ws.app.model.match.Match;
import es.udc.ws.app.model.ticket.Ticket;
import es.udc.ws.app.thrift.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.time.LocalDate;
import java.util.List;

public class ThriftMatchServiceImpl implements ThriftAppService.Iface {

    // [FUNC-1]
    @Override
    public ThriftMatchDto addMatch(ThriftMatchDto matchDto) throws ThriftInputValidationException {

        Match match = MatchToThriftMatchDtoConversor.toMatch(matchDto);

        try {
            Match addedMatch = MatchServiceFactory.getService().addMatch(match);
            return MatchToThriftMatchDtoConversor.toThriftMatchDto(addedMatch);
        } catch (InputValidationException e) {
            throw new ThriftInputValidationException(e.getMessage());
        }

    }

    // [FUNC-2]
    @Override
    public List<ThriftMatchDto> findByDates(String endDate) {

        List<Match> matches = MatchServiceFactory.getService().findByDates(LocalDate.now(), LocalDate.parse(endDate));

        return MatchToThriftMatchDtoConversor.toThriftMatchDtos(matches);

    }

    // [FUNC-3]
    @Override
    public ThriftMatchDto findById(long matchId) throws ThriftInstanceNotFoundException{
        try{

            Match match = MatchServiceFactory.getService().findMatch(matchId);

            return MatchToThriftMatchDtoConversor.toThriftMatchDto(match);
        } catch (InstanceNotFoundException e){
            throw new ThriftInstanceNotFoundException(e.getInstanceId().toString(), e.getInstanceType().
                    substring(e.getInstanceType().lastIndexOf('.') + 1));
        }
    }

    // [FUNC-4]
    @Override
    public ThriftTicketDto buyTicket(long matchId, String userId, String creditCardNumber, int howMany) throws ThriftInputValidationException,
            ThriftInstanceNotFoundException, ThriftAdquiredAfterException, ThriftSalesLimitReachedException {

        try {

            Ticket ticket = MatchServiceFactory.getService().buyTicket(matchId, userId, creditCardNumber, howMany);
            return TicketToThriftTicketDtoConversor.toThriftTicketDto(ticket);

        } catch (InstanceNotFoundException e) {
            throw new ThriftInstanceNotFoundException(e.getInstanceId().toString(),
                    e.getInstanceType().substring(e.getInstanceType().lastIndexOf('.') + 1));
        } catch (InputValidationException e) {
            throw new ThriftInputValidationException(e.getMessage());
        } catch (SalesLimitReachedException e) {
            throw new ThriftSalesLimitReachedException(e.getMatchId());
        } catch (AdquiredAfterException e) {
            throw new ThriftAdquiredAfterException(e.getMatchId());
        }

    }

    @Override
    public List<ThriftTicketDto> findByUser(String correoUsuario) {

        List<Ticket> tickets = MatchServiceFactory.getService().findByUser(correoUsuario);

        return TicketToThriftTicketDtoConversor.toThriftTicketDtos(tickets);

    }

    @Override
    public void getTickets(long saleId, String creditCardNumber) throws ThriftInputValidationException, ThriftInstanceNotFoundException, ThriftAlreadyPickedException, ThriftWrongCreditCardException{

        try {

            MatchServiceFactory.getService().giveTickets(saleId, creditCardNumber);

        } catch (InstanceNotFoundException e) {
            throw new ThriftInstanceNotFoundException(e.getInstanceId().toString(),
                    e.getInstanceType().substring(e.getInstanceType().lastIndexOf('.') + 1));
        } catch (InputValidationException e) {
            throw new ThriftInputValidationException(e.getMessage());
        } catch (AlreadyPickedException e){
            throw new ThriftAlreadyPickedException(e.getIdCompra());
        } catch (WrongCreditCardException e) {
            throw new ThriftWrongCreditCardException(e.getIdCompra(), e.getCreditCardNumber());
        }
    }

}