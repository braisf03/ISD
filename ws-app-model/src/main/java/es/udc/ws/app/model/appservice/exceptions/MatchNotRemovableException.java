package es.udc.ws.app.model.appservice.exceptions;

public class MatchNotRemovableException extends Exception{

    private Long matchId;

    public MatchNotRemovableException(Long matchId) {
        super("Match with id=\"" + matchId + "\n cannot be deleted because it has sales");
        this.matchId = matchId;
    }

    public Long getMovieId() {
        return matchId;
    }

    public void setMovieId(Long movieId) {
        this.matchId = movieId;
    }

}
