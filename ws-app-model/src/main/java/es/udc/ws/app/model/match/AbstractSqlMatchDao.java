package es.udc.ws.app.model.match;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

public abstract class AbstractSqlMatchDao implements SqlMatchDao {
    protected AbstractSqlMatchDao(){
    }

    @Override
    public Match findMatch(Connection conexion, Long idPartido)
            throws InstanceNotFoundException {
        String query = "Select fechaCreacion, fechaJugar, equipoVisitante,"
                + "precioEntrada, numeroAsientos, entradasVendidas FROM MatchT WHERE idPartido = ?";

        try (PreparedStatement preparedStatement = conexion.prepareStatement(query)){
            int i = 1;
            preparedStatement.setLong(i++, idPartido.longValue());
            ResultSet resultSet = preparedStatement.executeQuery();
            if(!resultSet.next()){
                throw new InstanceNotFoundException(idPartido, Match.class.getName());
            }

            i = 1;
            Timestamp fechaCreacionAsTimestamp = resultSet.getTimestamp(i++);
            LocalDateTime fechaCreacion = fechaCreacionAsTimestamp.toLocalDateTime();
            Timestamp fechaJugarAsTimestamp = resultSet.getTimestamp(i++);
            LocalDateTime fechaJugar = fechaJugarAsTimestamp.toLocalDateTime();
            String equipoVisitante = resultSet.getString(i++);
            float precioEntrada = resultSet.getFloat(i++);
            int numeroAsientos  = resultSet.getInt(i++);
            int entradasVendidas = resultSet.getInt(i++);


            return new Match(idPartido, equipoVisitante, fechaCreacion, fechaJugar,
                    precioEntrada, numeroAsientos, entradasVendidas);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<Match> findByDate(Connection conexion, LocalDate initDate, LocalDate endDate){
        String format = "YYYY-MM-dd HH:mm:ss";
        LocalDateTime initDateTime = LocalDateTime.of(initDate, LocalTime.of(00, 00, 00));
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.of(23, 59, 59));
        String query = "SELECT idPartido, equipoVisitante, numeroAsientos, precioEntrada, "
                + "fechaCreacion, fechaJugar , entradasVendidas FROM MatchT";
        if(initDate != null && endDate != null){
            query += " WHERE fechaJugar" + " BETWEEN '" + initDateTime.format(DateTimeFormatter.ofPattern(format)) +
                    "' AND '" +endDateTime.format(DateTimeFormatter.ofPattern(format)) + "'";
        }
        query += " ORDER BY fechaJugar";

        try (PreparedStatement preparedStatement = conexion.prepareStatement(query)){
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Match> matches = new ArrayList<>();

            while (resultSet.next()){
                int i = 1;
                Long idPartido = Long.valueOf(resultSet.getLong(i++));
                String equipoVisitante = resultSet.getString(i++);
                int numeroAsientos  = resultSet.getInt(i++);
                float precioEntrada = resultSet.getFloat(i++);
                Timestamp fechaCreacionAsTimestamp = resultSet.getTimestamp(i++);
                LocalDateTime fechaCreacion = fechaCreacionAsTimestamp.toLocalDateTime();
                Timestamp fechaJugarAsTimestamp = resultSet.getTimestamp(i++);
                LocalDateTime fechaJugar = fechaJugarAsTimestamp.toLocalDateTime();
                int entradasVendidas = resultSet.getInt(i++);

                matches.add(new Match(idPartido, equipoVisitante, fechaCreacion, fechaJugar,
                        precioEntrada, numeroAsientos,entradasVendidas));
            }

            return matches;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }




    @Override
    public void update (Connection connection, Match match)
            throws InstanceNotFoundException {

        String queryString = "UPDATE MatchT"
                + " SET equipoVisitante = ?, fechaJugar = ?, "
                + "precioEntrada = ?, numeroAsientos = ?, entradasVendidas = ? " +
                "WHERE idPartido = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            int i = 1;
            preparedStatement.setString(i++, match.getEquipoVisitante());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(match.getfechaJugar()));
            preparedStatement.setFloat(i++,match.getprecioEntrada());
            preparedStatement.setInt(i++,match.getnumeroAsientos());
            preparedStatement.setInt(i++, match.getentradasVendidas());
            preparedStatement.setLong(i++, match.getidPartido());


            /* Execute query. */
            int updatedRows = preparedStatement.executeUpdate();

            if (updatedRows == 0) {
                throw new InstanceNotFoundException(match.getidPartido(),
                        Match.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove (Connection connection, Long idPartido)
            throws InstanceNotFoundException {

        /* Create "query string*/
        String queryString = "DELETE FROM MatchT WHERE" + " idPartido = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            int i = 1;
            preparedStatement.setLong(i++, idPartido);

            int removedRows = preparedStatement.executeUpdate();

            if(removedRows == 0){
                throw new InstanceNotFoundException(idPartido,
                        Match.class.getName());
            }

        }catch (SQLException e){
            throw new RuntimeException(e);
        }

    }

}