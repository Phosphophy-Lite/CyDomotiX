package com.example.cydomotix.Service.Objects;

import com.example.cydomotix.Model.Objects.ConnectedObject;
import com.example.cydomotix.Model.Objects.ConsumptionInterval;
import com.example.cydomotix.Model.Objects.PowerChangeEvent;
import com.example.cydomotix.Model.Objects.UsageEvent;
import com.example.cydomotix.Repository.Objects.PowerChangeEventRepository;
import com.example.cydomotix.Repository.Objects.UsageEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class EnergyConsumptionService {
    @Autowired
    private UsageEventRepository usageEventRepository;

    @Autowired
    private PowerChangeEventRepository powerChangeEventRepository;

    /**
     * Calculer l'intervalle de consommation sur toute une période où l'objet est resté dans un même status (ON/OFF)
     * @param startEvent
     * @param endEvent
     * @return
     */
    public ConsumptionInterval calculateConsumptionInterval(UsageEvent startEvent, UsageEvent endEvent) {
        LocalDateTime start = startEvent.getTimestamp(); // Début de l'intervalle de consommation = début du changement de status
        // Si pas de changement de status par la suite pour marquer la fin de la période, considérer la date actuelle
        LocalDateTime end = (endEvent != null) ? endEvent.getTimestamp() : LocalDateTime.now();
        long durationMinutes = Duration.between(start, end).toMinutes(); // durée de l'intervalle

        // Si l'objet était éteint, pas de consommation
        if (!startEvent.isStatus()) {
            return new ConsumptionInterval(start, end, durationMinutes, 0.0, false);
        }

        // L'objet était allumé, on récupère les changements de puissance dans l'intervalle
        List<PowerChangeEvent> powerEvents = powerChangeEventRepository.findByConnectedObjectAndTimestampBetweenOrderByTimestampAsc(
                startEvent.getConnectedObject(), start, end
        );

        // Si aucun changement de puissance, on prend la puissance actuelle
        if (powerEvents.isEmpty()) {
            double currentPower = startEvent.getConnectedObject().getPower(); // en W
            double energyWh = (currentPower * durationMinutes) / 60.0;
            return new ConsumptionInterval(start, end, durationMinutes, energyWh, true);
        }

        // Sinon, on découpe l'intervalle en sous-intervalles selon les changements de puissance
        double totalEnergyWh = 0.0;
        LocalDateTime intervalStart = start;
        double lastPower = powerEvents.getFirst().getPower();

        for (PowerChangeEvent evt : powerEvents) {
            LocalDateTime intervalEnd = evt.getTimestamp();
            long minutes = Duration.between(intervalStart, intervalEnd).toMinutes();
            totalEnergyWh += (lastPower * minutes) / 60.0;
            intervalStart = evt.getTimestamp();
            lastPower = evt.getPower();
        }

        // Calculer pour l'intervalle final jusqu'à 'end'
        long remainingMinutes = Duration.between(intervalStart, end).toMinutes();
        totalEnergyWh += (lastPower * remainingMinutes) / 60.0;

        return new ConsumptionInterval(start, end, durationMinutes, totalEnergyWh, true);
    }

    /**
     * Calculer tous les intervalles de consommation d'un objet jusqu'à la date actuelle
     * @param connectedObject
     * @return
     */
    public List<ConsumptionInterval> calculateAllConsumptionIntervals(ConnectedObject connectedObject) {
        // Récupérer tous les événements d'utilisation (ON/OFF) triés par date
        List<UsageEvent> usageEvents = usageEventRepository.findByConnectedObjectOrderByTimestampAsc(connectedObject);
        List<ConsumptionInterval> intervals = new ArrayList<>();

        for (int i = 0; i < usageEvents.size(); i++) {
            UsageEvent startEvent = usageEvents.get(i);
            UsageEvent endEvent = (i + 1 < usageEvents.size()) ? usageEvents.get(i + 1) : null;

            ConsumptionInterval interval = calculateConsumptionInterval(startEvent, endEvent);
            intervals.add(interval);
        }
        return intervals;
    }

}

