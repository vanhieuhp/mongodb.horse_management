package com.horse.api;

import com.horse.business.service.HorseService;
import com.horse.data.dto.horse.HorseRequest;
import com.horse.data.dto.horse.HorseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/horses")
public class HorseController {

    @Autowired
    private HorseService horseService;

    @PostMapping
    public ResponseEntity<?> createHorse(@RequestBody @Valid HorseRequest horseRequest) {
        HorseResponse horseResponse = horseService.createHorse(horseRequest);
        return new ResponseEntity<>(horseResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateHorse(@RequestBody HorseRequest horseRequest, @PathVariable String id) {

        HorseResponse horseResponse =  horseService.updateHorse(id, horseRequest);
        return new ResponseEntity<>(horseResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHorse(@PathVariable String id) {

        horseService.deleteHorse(id);
        return new ResponseEntity<>("Delete Horse successfully", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getHorseInfo(@PathVariable String id) {

        HorseResponse horseResponse = horseService.findOne(id);
        return new ResponseEntity<>(horseResponse, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> findAll() {

        List<HorseResponse> horseResponseList = horseService.findAll();
        return new ResponseEntity<>(horseResponseList, HttpStatus.OK);
    }
}
