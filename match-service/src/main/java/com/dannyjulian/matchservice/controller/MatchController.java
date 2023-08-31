package com.dannyjulian.matchservice.controller;

import com.dannyjulian.matchservice.dto.MatchRequest;
import com.dannyjulian.matchservice.dto.MatchResponse;
import com.dannyjulian.matchservice.service.MatchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/match")
@Slf4j
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public MatchResponse findMatch(@RequestBody MatchRequest matchRequest) {
        log.info("Match request received: " + matchRequest);
        return matchService.doMatch(matchRequest);
    }

    //http://localhost:8082/api/match?guid=SOMEGUID
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MatchRequest> findMatchesByGuid(@RequestParam String guid) {
        return matchService.getMatchesByGuid(guid);
    }

}
