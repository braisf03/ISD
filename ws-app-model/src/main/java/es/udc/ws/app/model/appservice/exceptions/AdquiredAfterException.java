package es.udc.ws.app.model.appservice.exceptions;

public class AdquiredAfterException extends Exception{
    private Long matchId;

    public AdquiredAfterException(Long matchId) {
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
