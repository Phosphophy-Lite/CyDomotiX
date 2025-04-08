package com.example.cydomotix.Service.Objects;

import com.example.cydomotix.Model.Objects.ConnectedObject;
import com.example.cydomotix.Model.Objects.ConsumptionInterval;
import com.example.cydomotix.Model.Objects.PowerChangeEvent;
import com.example.cydomotix.Model.Objects.UsageEvent;
//import com.example.cydomotix.Repository.Objects.PowerChangeEventRepository;
//import com.example.cydomotix.Repository.Objects.UsageEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class EnergyConsumptionService {
/*
    @Autowired
    private UsageEventRepository usageEventRepo;

    @Autowired
    private PowerChangeEventRepository powerChangeRepo;

    public List<ConsumptionInterval> calculateEnergyConsumption(ConnectedObject object) {
        List<UsageEvent> usageEvents = usageEventRepo.findByConnectedObjectOrderByTimestamp(object);
        List<PowerChangeEvent> powerEvents = powerChangeRepo.findByConnectedObjectOrderByTimestamp(object);

        List<ConsumptionInterval> result = new ArrayList<>();

        LocalDateTime onTime = null;
        double currentPower = 0;

        for (int i = 0; i < usageEvents.size(); i++) {
            UsageEvent event = usageEvents.get(i);

            if (event.isStatus()) { // objet allumé
                onTime = event.getTimestamp(); // récupérer la date
                // Trouver la puissance au moment où l’objet a été allumé
                currentPower = getCurrentPowerAt(powerEvents, onTime);
            } else if (onTime != null) { // il y a eu un début de période de consommation (objet ON)
                LocalDateTime offTime = event.getTimestamp(); // date actuelle qui marque la fin de la période de consommation (objet devenu OFF)
                Duration duration = Duration.between(onTime, offTime); // récupérer l'intervalle de temps
                long minutes = duration.toMinutes();
                double hours = minutes / 60.0;

                double energyWh = currentPower * hours; // calcul de consommation

                result.add(new ConsumptionInterval(onTime, offTime, minutes, energyWh)); // ajouter un intervalle de consommation à l'historique

                onTime = null;
            }
        }

        return result;
    }

    /**
     * Récupérer la puissance à une date donnée
     * @param powerEvents
     * @param time
     * @return
     */
    /*
    private double getCurrentPowerAt(List<PowerChangeEvent> powerEvents, LocalDateTime time) {
        double lastPower = 0;
        for (PowerChangeEvent e : powerEvents) {
            if (!e.getTimestamp().isAfter(time)) {
                lastPower = e.getPower();
            } else {
                break;
            }
        }
        return lastPower;
    }

    /**
     * Calculer l'énergie consommée sur un intervalle de consommation
     * @param intervals
     * @return
     *//*
    public double calculateTotalEnergy(List<ConsumptionInterval> intervals) {
        return intervals.stream().mapToDouble(ConsumptionInterval::getEnergyWh).sum();
    }*/
}
