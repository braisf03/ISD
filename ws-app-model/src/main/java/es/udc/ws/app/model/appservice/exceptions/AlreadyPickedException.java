package es.udc.ws.app.model.appservice.exceptions;

public class AlreadyPickedException extends Exception{

    private Long idCompra;

    public AlreadyPickedException(Long idCompra) {
        super("Tickets with sale id \"" + idCompra + "\" have already been picked");
        this.idCompra=idCompra;
    }

    public Long getIdCompra(){
        return idCompra;
    }

    public void setIdCompra(Long matchId){
        this.idCompra = matchId;
    }


}
