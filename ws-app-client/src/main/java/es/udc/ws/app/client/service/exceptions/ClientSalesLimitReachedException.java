package es.udc.ws.app.client.service.exceptions;

public class ClientSalesLimitReachedException extends Exception{
    private Long matchId;

    public ClientSalesLimitReachedException(Long matchId) {
        super("Match with id \"" + matchId + "\" has not more tickets available");
        this.matchId=matchId;
    }

    public Long getMatchId(){
        return matchId;
    }

    public void setMatchId(Long matchId){
        this.matchId = matchId;
    }
}
