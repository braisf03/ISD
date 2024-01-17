package es.udc.ws.app.model.ticket;

import java.sql.Connection;
import java.util.List;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

public interface SqlTicketDao {

    public Ticket create(Connection connection, Ticket entrada);

    public Ticket find(Connection conexion, Long idCompra) throws InstanceNotFoundException;

    public List<Ticket> findbyUser(Connection conexion, String correoUsuario);

    public void update(Connection conexion, Ticket entrada)
            throws InstanceNotFoundException;

    public void remove(Connection conexion, Long idCompra)
            throws InstanceNotFoundException;
}
