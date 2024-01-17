package es.udc.ws.app.model.ticket;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;


public class Jdbc3CcSqlTicketDao extends AbstractSqlTicketDao{

    @Override
    public Ticket create(Connection connection, Ticket entrada) {

        String queryString = "INSERT INTO Ticket"
                + " (correoUsuario, numTarjetaBancaria, fechaHoraCompra,"
                + " idPartido, numEntradas, recogidas) VALUES (?, ?, ?, ?, ?, ?)";


        try (PreparedStatement preparedStatement = connection.prepareStatement(
                queryString, Statement.RETURN_GENERATED_KEYS)) {


            int i = 1;
            preparedStatement.setString(i++, entrada.getCorreoUsuario());
            preparedStatement.setString(i++, entrada.getNumTarjetaBancaria());
            Timestamp fechaHoraCompraStamp = entrada.getFechaHoraCompra() != null ? Timestamp.valueOf(entrada.getFechaHoraCompra()) : null;
            preparedStatement.setTimestamp(i++, fechaHoraCompraStamp);
            preparedStatement.setLong(i++, entrada.getIdPartido());
            preparedStatement.setInt(i++, entrada.getNumEntradas());
            preparedStatement.setInt(i++, 0);

            preparedStatement.executeUpdate();


            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (!resultSet.next()) {
                throw new SQLException(
                        "JDBC driver did not return generated key.");
            }
            Long saleId = resultSet.getLong(1);


            return new Ticket(saleId, entrada.getIdPartido(), entrada.getCorreoUsuario(),
                    entrada.getFechaHoraCompra(), entrada.getNumEntradas(), entrada.getNumTarjetaBancaria());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
