package es.udc.ws.app.model.appservice.exceptions;

public class SalesLimitReachedException extends Exception{
    private Long matchId;

    public SalesLimitReachedException(Long matchId) {
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
