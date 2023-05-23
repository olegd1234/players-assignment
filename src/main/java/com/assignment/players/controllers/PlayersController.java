package com.assignment.players.controllers;

import com.assignment.players.services.PlayersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("api/players")
public class PlayersController {

    private PlayersService playersService;

    @Autowired
    public PlayersController(PlayersService playersService) {
        this.playersService = playersService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getPlayers(
            @RequestParam(defaultValue = "3") int pageSize,
            @RequestParam(defaultValue = "1") int pageNum) {
        //TODO pageSize validation for max value
        Object ret = playersService.getPlayers(pageSize, pageNum);
        return ResponseEntity.ok(ret);
    }

    @GetMapping(path="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getPlayer(@PathVariable("id") String id) {
        Object ret = playersService.getPlayer(id);
        if (ret == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(ret);
    }

}
