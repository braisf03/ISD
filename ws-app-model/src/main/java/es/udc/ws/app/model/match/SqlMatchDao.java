package es.udc.ws.app.model.match;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

public interface SqlMatchDao {

    public Match createMatch(Connection connection, Match match);

    public Match findMatch(Connection connection, Long matchId)
        throws InstanceNotFoundException;

    public List<Match> findByDate(Connection connection, LocalDate dateTime1, LocalDate dateTime2);

    public void update(Connection connection, Match match)
            throws InstanceNotFoundException;

    public void remove(Connection connection, Long matchId)
            throws InstanceNotFoundException;
}
