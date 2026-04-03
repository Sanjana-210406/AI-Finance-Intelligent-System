package com.finance.intelligence;

import com.finance.intelligence.decision.DecisionEngine;

import java.time.LocalDateTime;
import java.util.*;

public class TestRunner {
    public static void main(String[] args) {
        System.out.println("\n===== DECISION TEST =====");
        testDecision(); 
    }

 public static void testDecision() {

    String d1 = DecisionEngine.getDecision(90, false, false, "NORMAL");
    System.out.println(d1); // CELEBRATE

    String d2 = DecisionEngine.getDecision(60, true, false, "NORMAL");
    System.out.println(d2); // WARNING

    String d3 = DecisionEngine.getDecision(60, false, true, "NORMAL");
    System.out.println(d3); // CONCERN

    String d4 = DecisionEngine.getDecision(60, false, false, "SPIKE");
    System.out.println(d4); // CONCERN

    String d5 = DecisionEngine.getDecision(60, false, false, "NORMAL");
    System.out.println(d5); // NEUTRAL
    }
}