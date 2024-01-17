package es.udc.ws.app.client.service.dto;

import java.time.LocalDateTime;

public class ClientMatchDto {
    private Long idPartido;
    private String equipoVisitante;
    private LocalDateTime fechaJugar;
    private float precioEntrada;
    private int numeroAsientos;
    private int entradasRestantes;

    public ClientMatchDto(Long idPartido, String equipoVisitante, LocalDateTime fechaJugar,
                          float precioEntrada, int numeroAsientos, int entradasRestantes){
        this.idPartido= idPartido;
        this.equipoVisitante = equipoVisitante;
        this.fechaJugar = fechaJugar;
        this.precioEntrada = precioEntrada;
        this.numeroAsientos = numeroAsientos;
        this.entradasRestantes = entradasRestantes;
    }

    public Long getMatchId() {
        return idPartido;
    }
    public void setMatchId(Long matchId) {
        this.idPartido = matchId;
    }

    public String getEquipoVisitante() {
        return equipoVisitante;
    }
    public void setEquipoVisitante(String equipoVisitante) {
        this.equipoVisitante = equipoVisitante;
    }

    public LocalDateTime getFechaJugar() {
        return fechaJugar;
    }
    public void setFechaJugar(LocalDateTime fechaJugar) {
        this.fechaJugar = fechaJugar;
    }

    public float getPrecioEntrada() {
        return precioEntrada;
    }
    public void setPrecioEntrada(float precioEntrada) {
        this.precioEntrada = precioEntrada;
    }

    public int getNumeroAsientos() {
        return numeroAsientos;
    }
    public void setNumeroAsientos(int numeroAsientos) {
        this.numeroAsientos = numeroAsientos;
    }

    public int getEntradasRestantes(){
        return entradasRestantes;
    }
    public void setEntradasRestantes(int entradasRestantes){
        this.entradasRestantes = entradasRestantes;
    }

    @Override
    public String toString() {
        return "MatchDto [matchId=" + idPartido + ", equipoVisitante=" + equipoVisitante
                + ", fechaJugar=" + fechaJugar
                + ", precioEntrada=" + precioEntrada + ", numeroAsientos=" + numeroAsientos
                + ", entradasRestantes="+ entradasRestantes +"]";
    }


}
