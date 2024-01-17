package es.udc.ws.app.test.model.appservice;

import static es.udc.ws.app.model.util.ModelConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import es.udc.ws.app.model.appservice.MatchService;
import es.udc.ws.app.model.appservice.MatchServiceFactory;
import es.udc.ws.app.model.appservice.exceptions.AdquiredAfterException;
import es.udc.ws.app.model.appservice.exceptions.AlreadyPickedException;
import es.udc.ws.app.model.appservice.exceptions.SalesLimitReachedException;
import es.udc.ws.app.model.appservice.exceptions.WrongCreditCardException;
import es.udc.ws.app.model.match.Match;
import es.udc.ws.app.model.match.SqlMatchDao;
import es.udc.ws.app.model.match.SqlMatchDaoFactory;
import es.udc.ws.app.model.ticket.SqlTicketDao;
import es.udc.ws.app.model.ticket.SqlTicketDaoFactory;
import es.udc.ws.app.model.ticket.Ticket;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.sql.SimpleDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class AppServiceTest {

    private final long NON_EXISTENT_MATCH_ID = -1;private final LocalDateTime PAST_DATE = LocalDateTime.of(2022, 1, 1, 17, 0, 0);

    private final String VALID_CREDIT_CARD_NUMBER = "1234567890123456";
    private final String INVALID_CREDIT_CARD_NUMBER = "";
    private final String VALID_EMAIL = "user@udc.es";
    private final String INVALID_EMAIL = "";

    private final int NUM_SEATS = 10;
    private final int INVALID_NUM_SEATS = 0;

    private static MatchService matchService = null;
    private static SqlMatchDao matchDao = null;
    private static SqlTicketDao ticketDao = null;

    @BeforeAll
    public static void init() {


        DataSource dataSource = new SimpleDataSource();

        DataSourceLocator.addDataSource(APP_DATA_SOURCE, dataSource);

        /* Add "dataSource" to "DataSourceLocator". */
        matchService = MatchServiceFactory.getService();

        ticketDao = SqlTicketDaoFactory.getDao();

        matchDao = SqlMatchDaoFactory.getDao();
    }

    private Match createValidMatch(LocalDateTime fechaJuego){
        return new Match("Real club deportivo da Coruña (2ª división B)", fechaJuego, 11, 350);
    }

    private Match createValidMatch(){
        return createValidMatch(LocalDateTime.of(2024, 1, 2, 17, 0, 0));
    }

    private Match createMatch(Match match) {
        DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);

        match.setfechaCreacion(LocalDateTime.now());

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
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private void removeMatch(Long matchId) {

        DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);

        try (Connection connection = dataSource.getConnection()) {

            try {

                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Do work. */
                matchDao.remove(connection, matchId);
                /* Commit. */
                connection.commit();

            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw new RuntimeException(e);
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

    private Ticket buyTicket(Long matchId){
        Ticket boughtTicket = null;
        try{
            boughtTicket = matchService.buyTicket(matchId, VALID_EMAIL, VALID_CREDIT_CARD_NUMBER, NUM_SEATS);
        }catch (InputValidationException | InstanceNotFoundException | SalesLimitReachedException |
                AdquiredAfterException e){
            throw new RuntimeException(e);
        }
        return  boughtTicket;
    }

    public Ticket findTicket(Long idCompra)
            throws InstanceNotFoundException {

        DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);

        try (Connection conexion = dataSource.getConnection()) {

            return ticketDao.find(conexion, idCompra);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void removeTicket(Long saleId) {

        DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);

        try (Connection connection = dataSource.getConnection()) {

            try {

                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Do work. */
                ticketDao.remove(connection, saleId);

                /* Commit. */
                connection.commit();

            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw new RuntimeException(e);
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


    @Test
    public void testAddMatchAndFindMatch() throws InputValidationException, InstanceNotFoundException {

        Match match = createValidMatch();
        Match addedMatch = null;

        try {

            // Create Movie
            LocalDateTime beforeCreationDate = LocalDateTime.now().withNano(0);

            addedMatch = matchService.addMatch(match);

            LocalDateTime afterCreationDate = LocalDateTime.now().withNano(0);

            // Find Movie
            Match foundMatch = matchService.findMatch(addedMatch.getidPartido());

            assertEquals(addedMatch, foundMatch);
            assertEquals(foundMatch.getEquipoVisitante(),match.getEquipoVisitante());
            assertEquals(foundMatch.getfechaJugar(),match.getfechaJugar());
            assertEquals(foundMatch.getprecioEntrada(),match.getprecioEntrada());
            assertEquals(foundMatch.getnumeroAsientos(),match.getnumeroAsientos());
            assertEquals(foundMatch.getentradasVendidas(),match.getentradasVendidas());
            assertTrue((foundMatch.getfechaCreacion().compareTo(beforeCreationDate) >= 0)
                    && (foundMatch.getfechaCreacion().compareTo(afterCreationDate) <= 0));

        } finally {
            // Clear Database
            if (addedMatch!=null) {
                removeMatch(addedMatch.getidPartido());
            }
        }
    }

    @Test
    public void testAddInvalidMatch() {

        // Check match visitor team not null
        assertThrows(InputValidationException.class, () -> {
            Match match = createValidMatch();
            match.setEquipoVisitante(null);
            Match addedMatch = matchService.addMatch(match);
            removeMatch(addedMatch.getidPartido());
        });

        // Check match visitor team not empty
        assertThrows(InputValidationException.class, () -> {
            Match match = createValidMatch();
            match.setEquipoVisitante("");
            Match addedMatch = matchService.addMatch(match);
            removeMatch(addedMatch.getidPartido());
        });

        // Check match seat number >= 0
        assertThrows(InputValidationException.class, () -> {
            Match match = createValidMatch();
            match.setnumeroAsientos((short) -1);
            Match addedMatch = matchService.addMatch(match);
            removeMatch(addedMatch.getidPartido());
        });

        // Check match seat number <= MAX_RUNTIME
        assertThrows(InputValidationException.class, () -> {
            Match match = createValidMatch();
            match.setnumeroAsientos((short) (MAX_ASIENTOS + 1));
            Match addedMatch = matchService.addMatch(match);
            removeMatch(addedMatch.getidPartido());
        });

        // Check match ticket number>=0
        assertThrows(InputValidationException.class, () -> {
            Match match = createValidMatch();
            match.setentradasVendidas((short) (-1));
            Match addedMatch = matchService.addMatch(match);
            removeMatch(addedMatch.getidPartido());
        });

        // Check movie description not null
        assertThrows(InputValidationException.class, () -> {
            Match match = createValidMatch();
            match.setentradasVendidas((short) (match.getnumeroAsientos() + 1));
            Match addedMatch = matchService.addMatch(match);
            removeMatch(addedMatch.getidPartido());
        });

        // Check ticket price >= 0
        assertThrows(InputValidationException.class, () -> {
            Match match = createValidMatch();
            match.setprecioEntrada((short) -1);
            Match addedMatch = matchService.addMatch(match);
            removeMatch(addedMatch.getidPartido());
        });

        // Check ticket price <= MAX_PRICE
        assertThrows(InputValidationException.class, () -> {
            Match match = createValidMatch();
            match.setprecioEntrada((short) (MAX_PRICE + 1));
            Match addedMatch = matchService.addMatch(match);
            removeMatch(addedMatch.getidPartido());
        });

        assertThrows(InputValidationException.class, () -> {
            Match match = createValidMatch();
            match.setfechaJugar(PAST_DATE);
            Match addedMatch = matchService.addMatch(match);
            removeMatch(addedMatch.getidPartido());
        });

        assertThrows(InputValidationException.class, () -> {
            Match match = createValidMatch();
            match.setfechaJugar(null);
            Match addedMatch = matchService.addMatch(match);
            removeMatch(addedMatch.getidPartido());
        });

    }

    @Test
    public void testFindNonExistentMatch() {
        assertThrows(InstanceNotFoundException.class, () -> matchService.findMatch(NON_EXISTENT_MATCH_ID));
    }


    @Test
    public void testFindMatches(){


        LocalDateTime fecha1 = LocalDateTime.of(2024, 2, 20, 17, 0, 0);
        LocalDateTime fecha2 = LocalDateTime.of(2024, 7, 15, 17, 0, 0);
        LocalDateTime fecha3 = LocalDateTime.of(2025, 1, 1, 17, 0, 0);

        List<Match> matches =new LinkedList<>();

        Match match1 = createMatch(createValidMatch(fecha1));
        matches.add(match1);

        Match match2 = createMatch(createValidMatch(fecha2));
        matches.add(match2);

        Match match3 = createMatch(createValidMatch(fecha3));
        matches.add(match3);

        try{
            List<Match> foundMatches = matchService.findByDates(fecha1.toLocalDate(), fecha3.toLocalDate());
            assertEquals(matches, foundMatches);

            foundMatches = matchService.findByDates(fecha2.toLocalDate(), fecha3.toLocalDate());
            assertEquals(2, foundMatches.size());

            foundMatches = matchService.findByDates(fecha1.toLocalDate().plusDays(1), fecha2.toLocalDate().minusDays(1));
            assertEquals(0, foundMatches.size());

            foundMatches = matchService.findByDates(fecha2.toLocalDate(), fecha1.toLocalDate());
            assertEquals(0, foundMatches.size());

        }finally {
            // Clear Database
            for (Match match : matches) {
                removeMatch(match.getidPartido());
            }
        }
    }


    @Test
    public void testBuyTickets()
            throws InstanceNotFoundException, InputValidationException, AdquiredAfterException, SalesLimitReachedException {

        Match match = createMatch(createValidMatch());
        Ticket sale = null;

        try {

            LocalDateTime beforeBuyDate = LocalDateTime.now().withNano(0);

            sale = matchService.buyTicket(match.getidPartido(), VALID_EMAIL, VALID_CREDIT_CARD_NUMBER, NUM_SEATS);

            LocalDateTime afterBuyDate = LocalDateTime.now().withNano(0);

            // Find sale
            Ticket foundSale = findTicket(sale.getIdCompra());

            // Check sale
            assertEquals(sale, foundSale);
            assertEquals(VALID_CREDIT_CARD_NUMBER, foundSale.getNumTarjetaBancaria());
            assertEquals(VALID_EMAIL, foundSale.getCorreoUsuario());
            assertEquals(match.getidPartido(), foundSale.getIdPartido());
            assertTrue((foundSale.getFechaHoraCompra().compareTo(beforeBuyDate) >= 0)
                    && (foundSale.getFechaHoraCompra().compareTo(afterBuyDate) <= 0));
            assertEquals(NUM_SEATS, sale.getNumEntradas());
            assertEquals(0, sale.getRecogidas());

            Match foundMatch = matchService.findMatch(match.getidPartido());

            assertEquals(match.getentradasVendidas() + NUM_SEATS, foundMatch.getentradasVendidas());

        } finally {
            // Clear database: remove sale (if created) and movie
            if (sale != null) {
                removeTicket(sale.getIdCompra());
            }
            removeMatch(match.getidPartido());
        }
    }

    @Test
    public void testBuyNonExistentMatch(){
        assertThrows(InstanceNotFoundException.class, () -> matchService.buyTicket(NON_EXISTENT_MATCH_ID, VALID_EMAIL, VALID_CREDIT_CARD_NUMBER, NUM_SEATS));
    }

    @Test
    public void testBuyTooManyTickets(){
        Match match = createMatch(createValidMatch());
        try {
            assertThrows(SalesLimitReachedException.class, () -> {
                Ticket ticket =matchService.buyTicket(match.getidPartido(), VALID_EMAIL, VALID_CREDIT_CARD_NUMBER, match.getnumeroAsientos() + 1);
                removeTicket(ticket.getIdCompra());
            });
        }finally {
            removeMatch(match.getidPartido());
        }
    }

    @Test
    public void testBuyWithInvalidInfo() {
        Match match = createMatch(createValidMatch());

        try {
            assertThrows(InputValidationException.class, () -> {
                Ticket ticket = matchService.buyTicket(match.getidPartido(), INVALID_EMAIL, VALID_CREDIT_CARD_NUMBER, NUM_SEATS);
                removeTicket(ticket.getIdCompra());
            });

            assertThrows(InputValidationException.class, () -> {
                Ticket ticket = matchService.buyTicket(match.getidPartido(), VALID_EMAIL, INVALID_CREDIT_CARD_NUMBER, NUM_SEATS);
                removeTicket(ticket.getIdCompra());
            });
            assertThrows(InputValidationException.class, () -> {
                Ticket ticket = matchService.buyTicket(match.getidPartido(), VALID_EMAIL, VALID_CREDIT_CARD_NUMBER, INVALID_NUM_SEATS);
                removeTicket(ticket.getIdCompra());
            });
        }finally {
            removeMatch(match.getidPartido());
        }
    }

    @Test
    public void testBuyAfterMatch() {
        Match match = createMatch(createValidMatch(PAST_DATE));
        try {
            assertThrows(AdquiredAfterException.class, () -> {
                Ticket ticket = matchService.buyTicket(match.getidPartido(), VALID_EMAIL, VALID_CREDIT_CARD_NUMBER, NUM_SEATS);
                removeTicket(ticket.getIdCompra());
            });
        } finally {
            removeMatch(match.getidPartido());
        }
    }


    @Test
    public void testFindByUser() throws AdquiredAfterException, SalesLimitReachedException, InstanceNotFoundException, InputValidationException {
        Match match = createMatch(createValidMatch());
        List<Ticket> tickets = new LinkedList<>();
        Ticket ticket1 = matchService.buyTicket(match.getidPartido(), "GuillenPasaCodigo", VALID_CREDIT_CARD_NUMBER, 2);
        tickets.add(ticket1);
        Ticket ticket2 = matchService.buyTicket(match.getidPartido(), "GuillenPasaCodigo", VALID_CREDIT_CARD_NUMBER, 4);
        tickets.add(ticket2);
        Ticket ticket3 = matchService.buyTicket(match.getidPartido(), "BraisPasaCodigo", VALID_CREDIT_CARD_NUMBER, 3);
        try {
            List<Ticket> foundTickets = matchService.findByUser("GuillenPasaCodigo");
            assertEquals(2, foundTickets.size());
            assertEquals(tickets, foundTickets);

            tickets.add(ticket3);

            foundTickets = matchService.findByUser("BraisPasaCodigo");
            assertEquals(1, foundTickets.size());

            foundTickets = matchService.findByUser("AntonPasaCodigo");
            assertEquals(0, foundTickets.size());

        } finally {
            // Clear Database
            for (Ticket ticket : tickets) {
                removeTicket(ticket.getIdCompra());
            }
        }
    }

    @Test
    public void testGiveTickets() throws InstanceNotFoundException, WrongCreditCardException, AlreadyPickedException, InputValidationException {
        Match match = createMatch(createValidMatch());
        Ticket ticket = null;
        try{
            ticket = buyTicket(match.getidPartido());

            matchService.giveTickets(ticket.getIdCompra(), VALID_CREDIT_CARD_NUMBER);
            Ticket modifiedSale = findTicket(ticket.getIdCompra());

            assertEquals(modifiedSale.getRecogidas(), 1);
        }finally {
            if(ticket != null) {
                removeTicket(ticket.getIdCompra());
            }
            removeMatch(match.getidPartido());
        }
    }

    @Test
    void testGiveWithInvalidCreditCard(){
        Match match = createMatch(createValidMatch());
        Ticket ticket = buyTicket(match.getidPartido());
        assertThrows(InputValidationException.class, () ->
                matchService.giveTickets(ticket.getIdCompra(), INVALID_CREDIT_CARD_NUMBER));
        removeTicket(ticket.getIdCompra());
        removeMatch(match.getidPartido());
    }


    @Test
    void testGiveAlreadyGivenTickets() throws InstanceNotFoundException, WrongCreditCardException, AlreadyPickedException, InputValidationException {
        Match match = createMatch(createValidMatch());
        Ticket ticket = buyTicket(match.getidPartido());
        matchService.giveTickets(ticket.getIdCompra(), VALID_CREDIT_CARD_NUMBER);

        assertThrows(AlreadyPickedException.class, () -> matchService.giveTickets(ticket.getIdCompra(), VALID_CREDIT_CARD_NUMBER));
        removeTicket(ticket.getIdCompra());
        removeMatch(match.getidPartido());
    }

    @Test
    void testGiveNonExistentTickets(){
        assertThrows(InstanceNotFoundException.class, () -> matchService.giveTickets(NON_EXISTENT_MATCH_ID, VALID_CREDIT_CARD_NUMBER));
    }


    @Test
    void testGiveTicketsWithWrongCreditCard(){
        String ANOTHER_CREDIT_CARD = "9876543210123456";

        Match match = createMatch(createValidMatch());
        Ticket ticket = buyTicket(match.getidPartido());

        assertThrows(WrongCreditCardException.class, () -> matchService.giveTickets(ticket.getIdCompra(), ANOTHER_CREDIT_CARD));
        removeTicket(ticket.getIdCompra());
        removeMatch(match.getidPartido());
    }

}