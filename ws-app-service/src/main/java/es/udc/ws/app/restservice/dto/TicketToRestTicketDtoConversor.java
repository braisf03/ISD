package es.udc.ws.app.restservice.dto;

import es.udc.ws.app.model.ticket.Ticket;

import java.util.ArrayList;
import java.util.List;

public class TicketToRestTicketDtoConversor {
    public static List<RestTicketDto> toRestTicketDtos(List<Ticket> tickets) {
        List<RestTicketDto> ticketDtos = new ArrayList<>(tickets.size());
        for (int i = 0; i < tickets.size(); i++) {
            Ticket ticket = tickets.get(i);
            ticketDtos.add(toRestTicketDto(ticket));
        }
        return ticketDtos;
    }

    public static RestTicketDto toRestTicketDto(Ticket ticket) {
        return new RestTicketDto(ticket.getIdCompra(), ticket.getIdPartido(),
                ticket.getCorreoUsuario(), ticket.getFechaHoraCompra().toString(),
                ticket.getNumTarjetaBancaria().substring(12), ticket.getNumEntradas(), ticket.getRecogidas());
    }
}
