package es.udc.ws.app.client.service.thrift;

import es.udc.ws.app.client.service.dto.ClientMatchDto;
import es.udc.ws.app.client.service.dto.ClientTicketDto;
import es.udc.ws.app.thrift.ThriftMatchDto;
import es.udc.ws.app.thrift.ThriftTicketDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ClientTicketDtoToThriftTicketDtoConversor {
    public static List<ClientTicketDto> toClientTicketDtos(List<ThriftTicketDto> tickets) {

        List<ClientTicketDto> clientTicketDtos = new ArrayList<>(tickets.size());

        for (ThriftTicketDto ticket : tickets) {
            clientTicketDtos.add(toClientTicketDto(ticket));
        }
        return clientTicketDtos;

    }

    private static ClientTicketDto toClientTicketDto(ThriftTicketDto ticket) {

        return new ClientTicketDto(
                ticket.getIdTicket(),
                ticket.getCorreoUsuario(),
                ticket.getNumTarjetaBancaria(),
                LocalDateTime.parse(ticket.getFechaHoraCompra()),
                ticket.getIdPartido(),
                ticket.getNumEntradas(),
                ticket.getRecogidas() == 1? "Sí": "No");

    }
}
