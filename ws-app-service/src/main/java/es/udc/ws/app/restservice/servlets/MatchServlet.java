package es.udc.ws.app.restservice.servlets;

import es.udc.ws.app.model.appservice.MatchServiceFactory;
import es.udc.ws.app.model.match.Match;
import es.udc.ws.app.restservice.dto.MatchToRestMatchDtoConversor;
import es.udc.ws.app.restservice.dto.RestMatchDto;
import es.udc.ws.app.restservice.json.JsonToRestMatchDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.RestHttpServletTemplate;
import es.udc.ws.util.servlet.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchServlet extends RestHttpServletTemplate {

    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp) throws IOException,
            InputValidationException {
        ServletUtils.checkEmptyPath(req);

        RestMatchDto matchDto = JsonToRestMatchDtoConversor.toRestMatchDto(req.getInputStream());
        Match match = MatchToRestMatchDtoConversor.toMatch(matchDto);

        match = MatchServiceFactory.getService().addMatch(match);

        matchDto = MatchToRestMatchDtoConversor.toRestMatchDto(match);
        String matchURL = ServletUtils.normalizePath(req.getRequestURL().toString()) + "/" + match.getidPartido();
        Map<String, String> headers = new HashMap<>(1);
        headers.put("Location", matchURL);
        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED,
                JsonToRestMatchDtoConversor.toObjectNode(matchDto), headers);
    }
    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp) throws IOException,
            InputValidationException, InstanceNotFoundException {

        String var = req.getPathInfo();
        if(var == null){
            var = "";
        }
        var = ServletUtils.normalizePath(var);

        if(var.equals("")){
            String endDate = req.getParameter("endDate");
            Date dateToConvert = new Date();

            LocalDate currentDate = dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            List<Match> matches = MatchServiceFactory.getService().findByDates(currentDate, LocalDate.parse(endDate));

            List<RestMatchDto> matchDtos = MatchToRestMatchDtoConversor.toRestMatchDtos(matches);
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                    JsonToRestMatchDtoConversor.toArrayNode(matchDtos), null);
        }else{

            String matchId = var.replaceAll("/", "");

            Match match = MatchServiceFactory.getService().findMatch(Long.parseLong(matchId));

            RestMatchDto matchDto = MatchToRestMatchDtoConversor.toRestMatchDto(match);
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                    JsonToRestMatchDtoConversor.toObjectNode(matchDto), null);
        }

    }
}
