package com.JavaTech.HeadQuarter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class StatsController {

//    private final SimpMessagingTemplate messagingTemplate;
//
//    private DashBoardController dashBoardController;
//    @Autowired
//    public StatsController(SimpMessagingTemplate messagingTemplate) {
//        this.messagingTemplate = messagingTemplate;
//    }
//
//    @PostMapping("/api/stats")
//    public void showData(@RequestParam("startDate") String startDateString,
//                         @RequestParam("endDate") String endDateString,
//                         @RequestParam("branch") String branch) throws ParseException {
//
//        messagingTemplate.convertAndSend("/topic/stats", dashBoardController.showData(startDateString, endDateString, branch));
//    }
}
