
package com.aiu.library.controller;

import com.aiu.library.service.WaitingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/waiting")
public class WaitingListController {

    @Autowired
    private WaitingListService waitingListService;

    @PostMapping("/join/{bookId}/{memberId}")
    public String join(@PathVariable int bookId, @PathVariable int memberId, Model model) {
        String message = waitingListService.joinWaitingList(bookId, memberId);
        model.addAttribute("message", message);
        return "waiting-result";
    }

    @GetMapping("/view/{bookId}")
    public String view(@PathVariable int bookId, Model model) {
        var queue = waitingListService.getWaitingList(bookId);
        model.addAttribute("bookId", bookId);
        model.addAttribute("waitingList", queue.getAll());
        model.addAttribute("queueSize", queue.size());
        return "waiting-list";
    }
}