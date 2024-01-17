package es.udc.ws.app.client.service.exceptions;

public class ClientAlreadyPickedException extends Exception{
    private Long idCompra;

    public ClientAlreadyPickedException(Long idCompra) {
        super("Tickets with sale ID \"" + idCompra + "\" have already been picked");
        this.idCompra=idCompra;
    }

    public Long getIdCompra(){
        return idCompra;
    }

    public void setIdCompra(Long matchId){
        this.idCompra = matchId;
    }
}
