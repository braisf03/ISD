package es.udc.ws.app.restservice.dto;

public class RestMatchDto {

    private Long idPartido;

    private String equipoVisitante;

    private String fechaJugar;

    private float precioEntrada;

    private int numeroAsientos;

    private int entradasVendidas;

    public RestMatchDto(Long idPartido, String equipoVisitante, String fechaJugar, float precioEntrada, int numeroAsientos, int entradasVendidas) {
        this.idPartido = idPartido;
        this.equipoVisitante = equipoVisitante;
        this.fechaJugar = fechaJugar;
        this.precioEntrada = precioEntrada;
        this.numeroAsientos = numeroAsientos;
        this.entradasVendidas = entradasVendidas;
    }

    public Long getIdPartido() {
        return idPartido;
    }

    public void setIdPartido(Long idPartido) {
        this.idPartido = idPartido;
    }

    public String getEquipoVisitante() {
        return equipoVisitante;
    }

    public void setEquipoVisitante(String equipoVisitante) {
        this.equipoVisitante = equipoVisitante;
    }

    public String getFechaJugar() {
        return fechaJugar;
    }

    public void setFechaJugar(String fechaJugar) {
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

    public int getEntradasVendidas() {
        return entradasVendidas;
    }

    public void setEntradasVendidas(int entradasVendidas) {
        this.entradasVendidas = entradasVendidas;
    }
}
