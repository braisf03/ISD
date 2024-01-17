package es.udc.ws.app.restservice.dto;

import es.udc.ws.app.model.match.Match;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class MatchToRestMatchDtoConversor {

    public static List<RestMatchDto> toRestMatchDtos(List<Match> partidos) {
        List<RestMatchDto> matchDtos = new ArrayList<>(partidos.size());
        for (int i = 0; i < partidos.size(); i++) {
            Match partido = partidos.get(i);
            matchDtos.add(toRestMatchDto(partido));
        }
        return matchDtos;
    }

    public static RestMatchDto toRestMatchDto(Match partido) {
        return new RestMatchDto(partido.getidPartido(), partido.getEquipoVisitante(), partido.getfechaJugar().toString(),
                partido.getprecioEntrada(), partido.getnumeroAsientos(), partido.getentradasVendidas());
    }

    public static Match toMatch(RestMatchDto partido) {
        return new Match(partido.getEquipoVisitante(), LocalDateTime.parse(partido.getFechaJugar()),
                partido.getPrecioEntrada(), partido.getNumeroAsientos());
    }

}
