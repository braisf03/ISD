package es.udc.ws.app.client.service.exceptions;

public class ClientAdquiredAfterException extends Exception{
    private Long matchId;

    public ClientAdquiredAfterException(Long matchId) {
        super("Match with id \"" + matchId + "\" has already been played");
        this.matchId=matchId;
    }

    public Long getMatchId(){
        return matchId;
    }

    public void setMatchId(Long matchId){
        this.matchId = matchId;
    }
}
