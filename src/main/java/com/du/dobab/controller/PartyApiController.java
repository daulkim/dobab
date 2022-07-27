package com.du.dobab.controller;

import com.du.dobab.dto.request.PartySave;
import com.du.dobab.service.PartyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class PartyApiController {

    private final PartyService partyService;

    @PostMapping("/api/v1/party")
    public void join(@RequestBody PartySave partySave) {
        partyService.save(partySave);
    }

    @DeleteMapping("/api/v1/party/{partyId}")
    public void leave(@PathVariable(name="partyId") Long id) {
        partyService.delete(id);
    }
}
