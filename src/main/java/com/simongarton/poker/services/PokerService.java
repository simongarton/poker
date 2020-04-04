package com.simongarton.poker.services;

import com.simongarton.poker.model.Result;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PokerService {

    public Result calculate(List<String> cards, int playerCount, List<String> communityCards) {
        Result result = new Result();
        result.setValue(0);
        result.setExplanation("Uncalculated");
        return result;
    }
}
