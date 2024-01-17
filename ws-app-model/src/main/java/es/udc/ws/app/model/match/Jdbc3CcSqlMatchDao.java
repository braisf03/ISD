package es.udc.ws.app.model.match;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;


public class Jdbc3CcSqlMatchDao extends AbstractSqlMatchDao{
    @Override
    public Match createMatch(Connection connection, Match match){

       String queryString = "INSERT INTO MatchT"
               + " (equipoVisitante, fechaCreacion, fechaJugar, precioEntrada, numeroAsientos, entradasVendidas)"
               + " VALUES (?, ?, ?, ?, ?, ?)";

       try (PreparedStatement preparedStatement = connection.prepareStatement(queryString,Statement.RETURN_GENERATED_KEYS)){

           /* Fill "preparedStatement". */
           int i = 1;

           preparedStatement.setString(i++, match.getEquipoVisitante());
           preparedStatement.setTimestamp(i++, Timestamp.valueOf(match.getfechaCreacion()));
           preparedStatement.setTimestamp(i++, Timestamp.valueOf(match.getfechaJugar()));
           preparedStatement.setFloat(i++,match.getprecioEntrada());
           preparedStatement.setInt(i++,match.getnumeroAsientos());
           preparedStatement.setInt(i++, match.getentradasVendidas());

           /* Execute query. */
           preparedStatement.executeUpdate();

           /* Get generated identifier. */
           ResultSet resultSet = preparedStatement.getGeneratedKeys();

           if (!resultSet.next()) {
               throw new SQLException(
                       "JDBC driver did not return generated key.");
           }
           Long idPartido = resultSet.getLong(1);

           return new Match(idPartido,match.getEquipoVisitante(),match.getfechaCreacion(),
                   match.getfechaJugar(),match.getprecioEntrada(),match.getnumeroAsientos(), match.getentradasVendidas());

       } catch (SQLException e) {
           throw new RuntimeException(e);
       }

    }

}
