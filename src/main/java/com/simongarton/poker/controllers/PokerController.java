package com.simongarton.poker.controllers;

import com.simongarton.poker.model.HandResponse;
import com.simongarton.poker.services.PokerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PokerController {

    @Autowired
    private PokerService pokerService;

    @GetMapping("/hand")
    public HandResponse getHandResponse(@RequestParam(required = true, name = "cards") String cards,
                                        @RequestParam(required = false, name = "community_cards") String communityCards,
                                        @RequestParam(required = true, name = "player_count") int playerCount,
                                        @RequestParam(required = false, name = "iterations") Integer iterations) {
        return pokerService.getHandResponse(cards, communityCards, playerCount, iterations);
    }
}
