package es.udc.ws.app.restservice.dto;

public class RestTicketDto {

    private Long idCompra;

    private Long idPartido;

    private String correoUsuario;

    private String fechaHoraCompra;

    private String numTarjetaBancaria;

    private int numEntradas;

    private int recogidas;

    public RestTicketDto(Long idCompra, Long idPartido, String correoUsuario, String fechaHoraCompra, String numTarjetaBancaria, int numEntradas, int recogidas) {
        this.idCompra = idCompra;
        this.idPartido = idPartido;
        this.correoUsuario = correoUsuario;
        this.fechaHoraCompra = fechaHoraCompra;
        this.numTarjetaBancaria = numTarjetaBancaria;
        this.numEntradas = numEntradas;
        this.recogidas = recogidas;
    }

    public Long getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(Long idCompra) {
        this.idCompra = idCompra;
    }

    public Long getIdPartido() {
        return idPartido;
    }

    public void setIdPartido(Long idPartido) {
        this.idPartido = idPartido;
    }

    public String getCorreoUsuario() {
        return correoUsuario;
    }

    public void setCorreoUsuario(String correoUsuario) {
        this.correoUsuario = correoUsuario;
    }

    public String getFechaHoraCompra() {
        return fechaHoraCompra;
    }

    public void setFechaHoraCompra(String fechaHoraCompra) {
        this.fechaHoraCompra = fechaHoraCompra;
    }

    public String getNumTarjetaBancaria() {
        return numTarjetaBancaria;
    }

    public void setNumTarjetaBancaria(String numTarjetaBancaria) {
        this.numTarjetaBancaria = numTarjetaBancaria;
    }

    public int getNumEntradas() {
        return numEntradas;
    }

    public void setNumEntradas(int numEntradas) {
        this.numEntradas = numEntradas;
    }

    public int getRecogidas() {
        return recogidas;
    }

    public void setRecogidas(int recogidas) {
        this.recogidas = recogidas;
    }
}
