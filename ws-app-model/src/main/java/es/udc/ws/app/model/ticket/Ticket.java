package es.udc.ws.app.model.ticket;


import java.time.LocalDateTime;
import java.util.Objects;

public class Ticket {

    private Long idCompra;

    private Long idPartido;

    private String correoUsuario;

    private LocalDateTime fechaHoraCompra;

    private String numTarjetaBancaria;

    private int numEntradas;

    private int recogidas;

    public Ticket (Long idCompra, Long idPartido, String correoUsuario,
                   LocalDateTime fechaHoraCompra, int numEntradas, String numTarjetaBancaria){
        this.idCompra=idCompra;
        this.idPartido=idPartido;
        this.correoUsuario=correoUsuario;
        this.fechaHoraCompra=(fechaHoraCompra != null) ? fechaHoraCompra.withNano(0): null;
        this.numEntradas = numEntradas;
        this.numTarjetaBancaria=numTarjetaBancaria;
        this.recogidas = 0;
    }

    public Ticket (Long idPartido, String correoUsuario, LocalDateTime fechaHoraCompra,
                  int numEntradas, String numTarjetaBancaria){
        this.idPartido=idPartido;
        this.correoUsuario=correoUsuario;
        this.fechaHoraCompra=(fechaHoraCompra != null) ? fechaHoraCompra.withNano(0): null;
        this.numEntradas = numEntradas;
        this.numTarjetaBancaria=numTarjetaBancaria;
        this.recogidas = 0;
    }

    public Ticket(Long idCompra, Long idPartido, String correoUsuario, LocalDateTime fechaHoraCompra, int numEntradas, String numTarjetaBancaria, int recogidas) {
        this.idCompra = idCompra;
        this.idPartido = idPartido;
        this.correoUsuario = correoUsuario;
        this.fechaHoraCompra = fechaHoraCompra;
        this.numEntradas = numEntradas;
        this.numTarjetaBancaria = numTarjetaBancaria;
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

    public LocalDateTime getFechaHoraCompra() {
        return fechaHoraCompra;
    }

    public void setFechaHoraCompra(LocalDateTime fechaHoraCompra) {
        this.fechaHoraCompra=(fechaHoraCompra != null) ? fechaHoraCompra.withNano(0): null;
    }

    public String getNumTarjetaBancaria() {
        return numTarjetaBancaria;
    }

    public void setNumTarjetaBancaria(String numTarjetaBancaria) {
        this.numTarjetaBancaria = numTarjetaBancaria;
    }

    public int getRecogidas() {
        return recogidas;
    }

    public void setRecogidas(int recogidas) {
        this.recogidas = recogidas;
    }

    public int getNumEntradas() {
        return numEntradas;
    }

    public void setNumEntradas(int numEntradas) {
        this.numEntradas = numEntradas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return numEntradas == ticket.numEntradas && recogidas == ticket.recogidas && idCompra.equals(ticket.idCompra) && idPartido.equals(ticket.idPartido) && correoUsuario.equals(ticket.correoUsuario) && fechaHoraCompra.equals(ticket.fechaHoraCompra) && numTarjetaBancaria.equals(ticket.numTarjetaBancaria);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCompra, idPartido, correoUsuario, fechaHoraCompra, numTarjetaBancaria, numEntradas, recogidas);
    }
}
