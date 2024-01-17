package es.udc.ws.app.model.ticket;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

public abstract class AbstractSqlTicketDao implements SqlTicketDao{
    protected AbstractSqlTicketDao(){
    }

    @Override
    public Ticket find(Connection conexion, Long idCompra)
            throws InstanceNotFoundException{
        String queryString = "SELECT idPartido, correoUsuario,"
                + "fechaHoraCompra, numEntradas, numTarjetaBancaria, recogidas FROM Ticket WHERE idCompra = ?";
        try (PreparedStatement preparedStatement = conexion.prepareStatement(queryString)){
            int i = 1;
            preparedStatement.setLong(i++, idCompra.longValue());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new InstanceNotFoundException(idCompra,
                        Ticket.class.getName());
            }

            i = 1;
            Long idPartido = resultSet.getLong(i++);
            String correoUsuario = resultSet.getString(i++);
            Timestamp fechaHoraCompraTimeStamp = resultSet.getTimestamp(i++);
            LocalDateTime fechaHoraCompra = fechaHoraCompraTimeStamp != null
                    ? fechaHoraCompraTimeStamp.toLocalDateTime()
                    : null;
            int numEntradas = resultSet.getInt(i++);
            String numTarjetaBancaria = resultSet.getString(i++);
            int recogidas = resultSet.getInt(i++);

            return new Ticket(idCompra, idPartido, correoUsuario, fechaHoraCompra,
                    numEntradas, numTarjetaBancaria, recogidas);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Ticket> findbyUser(Connection conexion, String correoUsuario) {
        String queryString = "SELECT idCompra, idPartido,"
                + "fechaHoraCompra, numEntradas, numTarjetaBancaria, recogidas FROM Ticket WHERE correoUsuario = ?";
        try (PreparedStatement preparedStatement = conexion.prepareStatement(queryString)){
            int i = 1;
            preparedStatement.setString(i++, correoUsuario);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Ticket> tickets = new ArrayList<>();
            while(resultSet.next()){
                i = 1;
                Long idCompra = resultSet.getLong(i++);
                Long idPartido = resultSet.getLong(i++);
                Timestamp fechaHoraCompraTimeStamp = resultSet.getTimestamp(i++);
                LocalDateTime fechaHoraCompra = fechaHoraCompraTimeStamp != null
                        ? fechaHoraCompraTimeStamp.toLocalDateTime()
                        : null;
                int numEntradas = resultSet.getInt(i++);
                String numTarjetaBancaria = resultSet.getString(i++);
                int recogidas = resultSet.getInt(i++);

                tickets.add(new Ticket(idCompra, idPartido, correoUsuario, fechaHoraCompra, numEntradas, numTarjetaBancaria, recogidas));
            }
            return tickets;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Connection connection, Ticket entrada)
            throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "UPDATE Ticket"
                + " SET  correoUsuario = ?, numTarjetaBancaria = ?, "
                + "fechaHoraCompra = ?, idPartido = ?, numEntradas = ?, recogidas = ? " +
                "WHERE idCompra = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setString(i++, entrada.getCorreoUsuario());
            preparedStatement.setString(i++, entrada.getNumTarjetaBancaria());
            //Timestamp date = entrada.getFechaHoraCompra() != null ? Timestamp.valueOf(entrada.getFechaHoraCompra()) : null;
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(entrada.getFechaHoraCompra()));
            preparedStatement.setLong(i++, entrada.getIdPartido());
            preparedStatement.setInt(i++, entrada.getNumEntradas());
            preparedStatement.setInt(i++, entrada.getRecogidas());
            preparedStatement.setLong(i++, entrada.getIdCompra());

            /* Execute query. */
            int updatedRows = preparedStatement.executeUpdate();

            if (updatedRows == 0) {
                throw new InstanceNotFoundException(entrada.getIdPartido(),
                        Ticket.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    @Override
    public void remove(Connection connection, Long idCompra)
            throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "DELETE FROM Ticket WHERE" + " idCompra = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, idCompra);

            /* Execute query. */
            int removedRows = preparedStatement.executeUpdate();

            if (removedRows == 0) {
                throw new InstanceNotFoundException(idCompra,
                        Ticket.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
