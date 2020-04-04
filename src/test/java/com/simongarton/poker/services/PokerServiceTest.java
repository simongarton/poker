package com.simongarton.poker.services;

import com.simongarton.poker.model.Result;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PokerServiceTest {

    @Autowired
    private PokerService pokerService;

    @Test
    void calculate() {
        Result result = pokerService.calculate(Collections.emptyList(), 0, Collections.emptyList());
        assertEquals(0, result.getValue());
    }
}