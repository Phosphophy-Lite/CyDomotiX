package com.example.cydomotix.Controller;

import com.example.cydomotix.Service.DeletionRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private DeletionRequestService deletionRequestService;

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
    public String approveDeletionRequest(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        deletionRequestService.approveRequest(id);
        redirectAttributes.addFlashAttribute("successMessage", "L'objet a bien été supprimé.");
        return "redirect:/admin/deletion-requests";
    }

    @PostMapping("/deletion-requests/{id}/reject")
    public String rejectDeletionRequest(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        deletionRequestService.rejectRequest(id);
        redirectAttributes.addFlashAttribute("successMessage", "La demande de suppression a bien été rejetée.");
        return "redirect:/admin/deletion-requests";
    }
}
