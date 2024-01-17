package es.udc.ws.app.model.match;

import java.time.LocalDateTime;

public class Match {
    private Long idPartido;
    private String equipoVisitante;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaJugar;
    private float precioEntrada;
    private int numeroAsientos;
    private int entradasVendidas;

    public Match(Long idPartido, String equipoVisitante, LocalDateTime fechaCreacion,
                 LocalDateTime fechaJugar, float precioEntrada, int numeroAsientos, int entradasVendidas){
        this.idPartido= idPartido;
        this.equipoVisitante = equipoVisitante;
        this.fechaCreacion = (fechaCreacion != null) ? fechaCreacion.withNano(0): null;
        this.fechaJugar = fechaJugar;
        this.precioEntrada = precioEntrada;
        this.numeroAsientos = numeroAsientos;
        this.entradasVendidas = entradasVendidas;

    }

    public Match(long id,String equipoVisitante,
                 LocalDateTime fechaJugar, float precioEntrada, int numeroAsientos){
        this.idPartido = id;
        this.equipoVisitante = equipoVisitante;
        this.fechaJugar = fechaJugar;
        this.precioEntrada = precioEntrada;
        this.numeroAsientos = numeroAsientos;
        this.entradasVendidas = 0;
    }
    public Match(String equipoVisitante, LocalDateTime fechaJugar, float precioEntrada, int numeroAsientos){
        this.equipoVisitante = equipoVisitante;
        this.fechaJugar = fechaJugar;
        this.precioEntrada = precioEntrada;
        this.numeroAsientos = numeroAsientos;
        this.entradasVendidas = 0;
    }

    public Long getidPartido(){
        return idPartido;
    }
    public void setidPartido(Long id){
        this.idPartido = id;
    }

    public String getEquipoVisitante(){
        return equipoVisitante;
    }
    public void setEquipoVisitante(String equipoVisitante){
        this.equipoVisitante = equipoVisitante;
    }

    public LocalDateTime getfechaCreacion() {
        return fechaCreacion;
    }
    public void setfechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = (fechaCreacion != null) ? fechaCreacion.withNano(0) : null;
    }

    public LocalDateTime getfechaJugar() {
        return fechaJugar;
    }
    public void setfechaJugar(LocalDateTime fechaJugar) {
        this.fechaJugar = (fechaJugar != null) ? fechaJugar.withNano(0) : null;
    }

    public float getprecioEntrada() {
        return precioEntrada;
    }
    public void setprecioEntrada(float price) {
        this.precioEntrada = price;
    }

    public int getnumeroAsientos(){
        return numeroAsientos;
    }
    public void setnumeroAsientos(int numeroAsientos){
        this.numeroAsientos = numeroAsientos;
    }

    public int getentradasVendidas() {
        return entradasVendidas;
    }

    public void setentradasVendidas(int entradasVendidas) {
        this.entradasVendidas = entradasVendidas;
    }

    @Override
    public int hashCode(){
        final int prime = 31;
        int result = 1;

        result = prime * result + ((idPartido == null) ? 0 : idPartido.hashCode());
        result = prime * result + ((equipoVisitante == null) ? 0 : equipoVisitante.hashCode());
        result = prime * result + ((fechaCreacion == null) ? 0 : fechaCreacion.hashCode());
        result = prime * result + ((fechaJugar == null) ? 0 : fechaJugar.hashCode());
        result = prime * result + Float.floatToIntBits(precioEntrada);
        result = prime * result + numeroAsientos;
        result = prime * result + entradasVendidas;

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Match other = (Match) obj;
        if (fechaCreacion == null) {
            if (other.fechaCreacion != null)
                return false;
        } else if (!fechaCreacion.equals(other.fechaCreacion))
            return false;
        if (equipoVisitante == null) {
            if (other.equipoVisitante != null)
                return false;
        } else if (!equipoVisitante.equals(other.equipoVisitante))
            return false;
        if (idPartido == null) {
            if (other.idPartido != null)
                return false;
        } else if (!idPartido.equals(other.idPartido))
            return false;
        if (Float.floatToIntBits(precioEntrada) != Float.floatToIntBits(other.precioEntrada))
            return false;
        if (numeroAsientos != other.numeroAsientos)
            return false;
        if (entradasVendidas != other.entradasVendidas)
            return false;
        return true;
    }
}
