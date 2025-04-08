package com.example.cydomotix.Controller.Objects;

import com.example.cydomotix.Service.Objects.EnergyConsumptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/gestion/stats")
public class StatsController {
/*
    @Autowired
    private EnergyConsumptionService energyConsumptionService;

    @GetMapping("/consumption/{id}")
    public double getConsumption(
            @PathVariable Integer id,
            @RequestParam LocalDateTime from,
            @RequestParam LocalDateTime to
    ) {
        return energyConsumptionService.calculateEnergyConsumption(id, from, to);
    }*/
}

