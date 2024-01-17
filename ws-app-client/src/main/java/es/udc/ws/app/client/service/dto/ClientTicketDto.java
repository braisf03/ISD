package es.udc.ws.app.client.service.dto;

import java.time.LocalDateTime;

public class ClientTicketDto {

    private Long idCompra;

    private String correoUsuario;

    private String numTarjetaBancaria;

    private LocalDateTime fechaHoraCompra;
    private Long idPartido;
    private int numEntradas;
    private String recogidas;

    public ClientTicketDto(Long idCompra, String correoUsuario, String numTarjetaBancaria, LocalDateTime fechaHoraCompra,
                           Long idPartido, int numEntradas, String recogidas) {
        this.correoUsuario = correoUsuario;
        this.numTarjetaBancaria = numTarjetaBancaria;
        this.fechaHoraCompra = fechaHoraCompra;
        this.idPartido = idPartido;
        this.numEntradas = numEntradas;
        this.recogidas = recogidas;
        this.idCompra = idCompra;

    }

    public Long getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(Long saleId) {
        this.idCompra = saleId;
    }

    public Long getIdPartido() {
        return idPartido;
    }

    public void setIdPartido(Long movieId) {
        this.idPartido = movieId;
    }

    public String getCorreoUsuario() {
        return correoUsuario;
    }

    public void setCorreoUsuario(String correoUsuario) {
        this.correoUsuario = correoUsuario;
    }

    public String getNumTarjetaBancaria() {
        return numTarjetaBancaria;
    }

    public void setNumTarjetaBancaria(String numTarjetaBancaria) {
        this.numTarjetaBancaria = numTarjetaBancaria;
    }


    public LocalDateTime getFechaHoraCompra() {
        return fechaHoraCompra;
    }

    public void setFechaHoraCompra(LocalDateTime fechaHoraCompra) {
        this.fechaHoraCompra = fechaHoraCompra;
    }


    public int getNumEntradas() {
        return numEntradas;
    }

    public void setNumEntradas(int numEntradas) {
        this.numEntradas = numEntradas;
    }


    public String getrecogidas() {
        return recogidas;
    }

    public void setRecogidas(String recogidas) {
        this.recogidas = recogidas;
    }


}
