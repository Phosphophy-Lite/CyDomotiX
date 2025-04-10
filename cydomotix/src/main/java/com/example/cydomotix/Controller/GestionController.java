package com.example.cydomotix.Controller;

import com.example.cydomotix.Model.Objects.ConnectedObject;
import com.example.cydomotix.Model.Objects.ConsumptionInterval;
import com.example.cydomotix.Service.Objects.ConnectedObjectService;
import com.example.cydomotix.Service.Objects.EnergyConsumptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/gestion")
public class GestionController {

    @Autowired
    private EnergyConsumptionService energyConsumptionService;

    @Autowired
    private ConnectedObjectService connectedObjectService;


    @GetMapping
    public String viewGestionDashboard() {
        return "gestion/gestion";
    }

    @GetMapping("/history/{id}/intervals")
    @ResponseBody
    public List<ConsumptionInterval> getConnectedObjectConsumptionHistory(@PathVariable("id") Integer id) {
        ConnectedObject connectedObject = connectedObjectService.getConnectedObjectById(id);
        return energyConsumptionService.calculateAllConsumptionIntervals(connectedObject).reversed();
    }

    @GetMapping("/history")
    public String showConsumptionHistoryPage(Model model) {
        List<ConnectedObject> connectedObjects = connectedObjectService.getAllConnectedObjects();
        model.addAttribute("connectedObjects", connectedObjects);
        return "gestion/consumption-history";
    }

    @GetMapping("/history/{id}/intervals/range")
    @ResponseBody
    public List<ConsumptionInterval> getConsumptionIntervalsForPeriod(
            @PathVariable("id") Integer id,
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        ConnectedObject connectedObject = connectedObjectService.getConnectedObjectById(id);
        System.out.println(energyConsumptionService.calculateTotalConsumptionBetween(connectedObject, start, end));
        return energyConsumptionService.calculateConsumptionIntervalsBetween(connectedObject, start, end).reversed();
    }

    @GetMapping("/stats")
    public String getAllTotalConsumptions(Model model) {

        Map<Integer, Double> totalConsumptions = new HashMap<>();
        List<ConnectedObject> connectedObjects = connectedObjectService.getAllConnectedObjects();

        for (ConnectedObject object : connectedObjects) {
            double totalWh = energyConsumptionService.calculateTotalConsumption(object);
            totalConsumptions.put(object.getId(), totalWh);
        }

        model.addAttribute("totalConsumptions", totalConsumptions);
        model.addAttribute("connectedObjects", connectedObjects); // pour afficher noms, etc.

        return "gestion/stats";
    }

    public record ConsumptionStatDTO(Integer objectId, String name, double totalWh) {}

    @GetMapping("/stats/range")
    @ResponseBody
    public List<ConsumptionStatDTO> getAllTotalConsumptionsForPeriod(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        List<ConnectedObject> connectedObjects = connectedObjectService.getAllConnectedObjects();
        List<ConsumptionStatDTO> stats = new ArrayList<>();

        for (ConnectedObject object : connectedObjects) {
            double totalWh = energyConsumptionService.calculateTotalConsumptionBetween(object, start, end);
            stats.add(new ConsumptionStatDTO(object.getId(), object.getName(), totalWh));
        }
        return stats;
    }
}
