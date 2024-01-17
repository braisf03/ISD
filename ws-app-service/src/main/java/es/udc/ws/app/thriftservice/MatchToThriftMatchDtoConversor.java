package es.udc.ws.app.thriftservice;

import es.udc.ws.app.model.match.Match;
import es.udc.ws.app.thrift.ThriftMatchDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MatchToThriftMatchDtoConversor {

    public static Match toMatch(ThriftMatchDto match) {
        return new Match( match.getEquipoVisitante(), LocalDateTime.parse(match.getFechaJugar()), (float) (match.getPrecioEntrada()), match.getNumeroAsientos());
    }

    public static List<ThriftMatchDto> toThriftMatchDtos(List<Match> matches) {

        List<ThriftMatchDto> dtos = new ArrayList<>(matches.size());

        for (Match match : matches) {
            dtos.add(toThriftMatchDto(match));
        }
        return dtos;

    }

    public static ThriftMatchDto toThriftMatchDto(Match match) {

        return new ThriftMatchDto(match.getidPartido(), match.getEquipoVisitante(), match.getfechaJugar().toString(),
                match.getprecioEntrada(), match.getnumeroAsientos(), match.getentradasVendidas());

    }
}