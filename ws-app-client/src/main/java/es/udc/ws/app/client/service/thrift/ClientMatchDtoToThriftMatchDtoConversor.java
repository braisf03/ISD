package es.udc.ws.app.client.service.thrift;

import es.udc.ws.app.client.service.dto.ClientMatchDto;
import es.udc.ws.app.thrift.ThriftMatchDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ClientMatchDtoToThriftMatchDtoConversor {
    public static ThriftMatchDto toThriftMatchDto(
            ClientMatchDto clientMatchDto) {

        Long matchId = clientMatchDto.getMatchId();

        return new ThriftMatchDto(
                matchId == null ? -1 : matchId.longValue(),
                clientMatchDto.getEquipoVisitante(),
                clientMatchDto.getFechaJugar().toString(),
                clientMatchDto.getPrecioEntrada(),
                clientMatchDto.getNumeroAsientos(),
                0);

    }

    public static List<ClientMatchDto> toClientMatchDtos(List<ThriftMatchDto> matches) {

        List<ClientMatchDto> clientMatchDtos = new ArrayList<>(matches.size());

        for (ThriftMatchDto match : matches) {
            clientMatchDtos.add(toClientMatchDto(match));
        }
        return clientMatchDtos;

    }

    public static ClientMatchDto toClientMatchDto(ThriftMatchDto match) {

        return new ClientMatchDto(
                match.getIdPartido(),
                match.getEquipoVisitante(),
                LocalDateTime.parse(match.getFechaJugar()),
                (float) match.getPrecioEntrada(),
                match.getNumeroAsientos(),
                (match.getNumeroAsientos()-match.getEntradasVendidas()));

    }
}
