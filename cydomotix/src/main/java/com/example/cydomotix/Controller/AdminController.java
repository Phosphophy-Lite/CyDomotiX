package com.example.cydomotix.Controller;

import com.example.cydomotix.Model.Objects.ConnectedObject;
import com.example.cydomotix.Service.DeletionRequestService;
import com.example.cydomotix.Service.Objects.ConnectedObjectService;
import com.example.cydomotix.Service.Objects.EnergyConsumptionService;
import com.example.cydomotix.Service.UserActionService;
import com.example.cydomotix.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private DeletionRequestService deletionRequestService;

    @Autowired
    private UserService userService;
    @Autowired
    private UserActionService userActionService;
    @Autowired
    private EnergyConsumptionService energyConsumptionService;
    @Autowired
    private ConnectedObjectService connectedObjectService;

    @GetMapping
    public String viewAdministrationDashboard() {
        return "admin/administration";
    }

    @GetMapping("/deletion-requests")
    public String viewDeletionRequests(Model model) {
        model.addAttribute("requests", deletionRequestService.getAllDeletionRequests());
        return "admin/deletion-requests";
    }

    @PostMapping("/deletion-requests/{id}/approve")
    public String approveDeletionRequest(@PathVariable Integer id, RedirectAttributes redirectAttributes, Principal principal) {
        deletionRequestService.approveRequest(id, principal.getName());
        redirectAttributes.addFlashAttribute("successMessage", "L'objet a bien été supprimé.");
        return "redirect:/admin/deletion-requests";
    }

    @PostMapping("/deletion-requests/{id}/reject")
    public String rejectDeletionRequest(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        deletionRequestService.rejectRequest(id);
        redirectAttributes.addFlashAttribute("successMessage", "La demande de suppression a bien été rejetée.");
        return "redirect:/admin/deletion-requests";
    }

    @GetMapping("/registration-requests")
    public String viewRegistrationRequests(Model model) {
        model.addAttribute("pendingUsers", userService.getUnapprovedUsers());
        return "admin/registration-requests";
    }

    @PostMapping("/registration-requests/{id}/approve")
    public String approveRegistrationRequest(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        userService.approveNewUser(id);
        redirectAttributes.addFlashAttribute("successMessage", "L'inscription de cet utilisateur a bien été approuvée.");
        return "redirect:/admin/registration-requests";
    }

    @PostMapping("/registration-requests/{id}/reject")
    public String rejectRegistrationRequest(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        userService.rejectNewUser(id);
        redirectAttributes.addFlashAttribute("successMessage", "La demande d'inscription a bien été rejetée.");
        return "redirect:/admin/registration-requests";
    }

    @GetMapping("/history")
    public String viewHistory(Model model) {
        // Afficher l'historique d'actions en commençant au dernier enregistrement (le plus récent)
        model.addAttribute("userActions", userActionService.getAllUserActions().reversed());
        return "admin/history";
    }

    @GetMapping("/stats")
    public String getGlobalStats() {
        return "admin/stats";
    }

    public record DurationStatDTO(Integer objectId, String name, long durationMinutes) {}

    @GetMapping("/stats/range")
    @ResponseBody
    public Map<String, Object> getStatsForPeriod(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime end) {

        double totalHouseConsumption = energyConsumptionService
                .calculateTotalHouseConsumptionBetween(connectedObjectService.getAllConnectedObjects(), start, end);

        double loginRate = userActionService.calculateLoginRateBetween(userService.getAllVerifiedUsers(),start, end);

        List<ConnectedObject> connectedObjects = connectedObjectService.getAllConnectedObjects();
        List<DurationStatDTO> durationStats = new ArrayList<>();

        for (ConnectedObject object : connectedObjects) {
            long durationMinutesTotal = energyConsumptionService.calculateTotalDurationBetween(object, start, end);
            durationStats.add(new DurationStatDTO(object.getId(), object.getName(), durationMinutesTotal));
        }

        Map<String, Object> result = new HashMap<>();
        result.put("totalHouseConsumption", totalHouseConsumption);
        result.put("loginRate", loginRate);
        result.put("durationStats", durationStats);

        return result;
    }

}
