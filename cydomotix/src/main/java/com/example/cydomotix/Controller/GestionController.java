package com.example.cydomotix.Controller;

import com.example.cydomotix.Model.Objects.ConnectedObject;
import com.example.cydomotix.Model.Objects.ConsumptionInterval;
import com.example.cydomotix.Service.Objects.ConnectedObjectService;
import com.example.cydomotix.Service.Objects.EnergyConsumptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

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
}
