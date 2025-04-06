package com.example.cydomotix.Controller;

import com.example.cydomotix.Service.DeletionRequestService;
import com.example.cydomotix.Service.UserActionService;
import com.example.cydomotix.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private DeletionRequestService deletionRequestService;

    @Autowired
    private UserService userService;
    @Autowired
    private UserActionService userActionService;

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
        model.addAttribute("userActions", userActionService.getAllUserActions());
        return "admin/history";
    }
}
