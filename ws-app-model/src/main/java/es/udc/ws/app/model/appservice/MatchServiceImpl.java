package es.udc.ws.app.model.appservice;

import es.udc.ws.app.model.appservice.exceptions.AdquiredAfterException;
import es.udc.ws.app.model.appservice.exceptions.AlreadyPickedException;
import es.udc.ws.app.model.appservice.exceptions.SalesLimitReachedException;
import es.udc.ws.app.model.appservice.exceptions.WrongCreditCardException;
import es.udc.ws.app.model.appservice.validation.CustomPropertyValidator;
import es.udc.ws.app.model.match.Match;
import es.udc.ws.app.model.match.SqlMatchDao;
import es.udc.ws.app.model.match.SqlMatchDaoFactory;
import es.udc.ws.app.model.ticket.SqlTicketDao;
import es.udc.ws.app.model.ticket.SqlTicketDaoFactory;
import es.udc.ws.app.model.ticket.Ticket;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static es.udc.ws.app.model.util.ModelConstants.*;

public class MatchServiceImpl implements MatchService{


    private final DataSource dataSource;

    private SqlMatchDao matchDao = null;

    private SqlTicketDao ticketDao = null;

    public MatchServiceImpl() {
        dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);
        matchDao = SqlMatchDaoFactory.getDao();
        ticketDao = SqlTicketDaoFactory.getDao();
    }

    private void validateMatch(Match match) throws InputValidationException {
        CustomPropertyValidator.validateMandatoryString("equipoVisitante", match.getEquipoVisitante());
        CustomPropertyValidator.validateDouble("precioEntrada", match.getprecioEntrada(), 0, MAX_PRICE);
        CustomPropertyValidator.validateLong("numeroAsientos", match.getnumeroAsientos(), 1, MAX_ASIENTOS);
        CustomPropertyValidator.validateLong("entradasVendidas", match.getentradasVendidas(), 0, match.getnumeroAsientos());
        CustomPropertyValidator.validateDates(match.getfechaCreacion(), match.getfechaJugar());
    }

    @Override
    public Match addMatch(Match match) throws InputValidationException {

        match.setfechaCreacion(LocalDateTime.now());
        validateMatch(match);

        try (Connection connection = dataSource.getConnection()) {

            try {

                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Do work. */
                Match createdMatch = matchDao.createMatch(connection, match);

                /* Commit. */
                connection.commit();

                return createdMatch;

            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Match findMatch(Long movieId) throws InstanceNotFoundException {

        try (Connection connection = dataSource.getConnection()) {
            return matchDao.findMatch(connection, movieId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Match> findByDates(LocalDate initDate, LocalDate endDate) {
        try (Connection connection = dataSource.getConnection()) {
            return matchDao.findByDate(connection, initDate, endDate);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Ticket buyTicket(Long idPartido, String correoUsuario, String creditCardNumber, int howMany)
            throws InstanceNotFoundException, InputValidationException, SalesLimitReachedException, AdquiredAfterException{

        CustomPropertyValidator.validateMandatoryString("correoUsuario",  correoUsuario);
        CustomPropertyValidator.validateCreditCard(creditCardNumber);
        CustomPropertyValidator.validateLong("numEntradas", howMany, 1);

        try (Connection conexion = dataSource.getConnection()) {

            try {
                /* Prepare connection. */
                conexion.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                conexion.setAutoCommit(false);

                /* Do work. */
                Match partido = matchDao.findMatch(conexion, idPartido);


                partido.setentradasVendidas(partido.getentradasVendidas() + howMany);
                LocalDateTime fechaAdquirir = LocalDateTime.now();
                try{
                    if(partido.getnumeroAsientos() - partido.getentradasVendidas() < 0) {
                        throw new SalesLimitReachedException(partido.getidPartido());
                    }
                    if(fechaAdquirir.isAfter(partido.getfechaJugar())) {
                        throw new AdquiredAfterException(partido.getidPartido());
                    }

                    Ticket entrada = ticketDao.create(conexion, new Ticket(idPartido, correoUsuario,
                            fechaAdquirir, howMany, creditCardNumber));
                    matchDao.update(conexion, partido);

                    /* Commit. */
                    conexion.commit();
                    return entrada;

                } catch (SalesLimitReachedException | AdquiredAfterException e ) {
                    conexion.commit();
                    throw e;
                }
            } catch (InstanceNotFoundException e) {
                conexion.commit();
                throw e;
            } catch (SQLException e) {
                conexion.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                conexion.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    public List<Ticket> findByUser(String correoUsuario) {

        try (Connection conexion = dataSource.getConnection()) {

            return ticketDao.findbyUser(conexion, correoUsuario);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void giveTickets(Long idCompra,String numTarjetaBancaria)
            throws InstanceNotFoundException, AlreadyPickedException, WrongCreditCardException, InputValidationException {

        CustomPropertyValidator.validateCreditCard(numTarjetaBancaria);
        try (Connection conexion = dataSource.getConnection()) {
            Ticket entrada = ticketDao.find(conexion, idCompra);
            try{
                conexion.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                conexion.setAutoCommit(false);

                if(!entrada.getNumTarjetaBancaria().equals(numTarjetaBancaria)) {
                    throw new WrongCreditCardException(entrada.getIdCompra(), numTarjetaBancaria);
                }

                if( (entrada.getRecogidas() == 1)) {
                    throw new AlreadyPickedException(entrada.getIdCompra());
                }

                entrada.setRecogidas(1);
                ticketDao.update(conexion, entrada);
                conexion.commit();


            }catch (AlreadyPickedException e) {
                conexion.commit();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}


