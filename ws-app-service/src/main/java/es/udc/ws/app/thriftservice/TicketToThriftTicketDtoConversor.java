package es.udc.ws.app.thriftservice;

import es.udc.ws.app.model.match.Match;
import es.udc.ws.app.model.ticket.Ticket;
import es.udc.ws.app.thrift.ThriftMatchDto;
import es.udc.ws.app.thrift.ThriftTicketDto;

import java.util.ArrayList;
import java.util.List;

public class TicketToThriftTicketDtoConversor {

    public static ThriftTicketDto toThriftTicketDto(Ticket ticket) {

        return new ThriftTicketDto(ticket.getIdCompra(), ticket.getIdPartido(), ticket.getCorreoUsuario(),
                ticket.getFechaHoraCompra().toString(), ticket.getNumTarjetaBancaria().substring(12), ticket.getNumEntradas(), ticket.getRecogidas());
    }

    public static List<ThriftTicketDto> toThriftTicketDtos(List<Ticket> tickets) {

        List<ThriftTicketDto> dtos = new ArrayList<>(tickets.size());

        for (Ticket ticket : tickets) {
            dtos.add(toThriftTicketDto(ticket));
        }
        return dtos;

    }
}