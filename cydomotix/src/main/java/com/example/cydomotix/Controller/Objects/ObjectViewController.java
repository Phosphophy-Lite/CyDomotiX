package com.example.cydomotix.Controller.Objects;

import com.example.cydomotix.Model.Objects.ConnectedObject;
import com.example.cydomotix.Model.Users.User;
import com.example.cydomotix.Service.Objects.ConnectedObjectService;
import com.example.cydomotix.Service.Objects.ObjectAttributeService;
import com.example.cydomotix.Service.Objects.ObjectTypeService;
import com.example.cydomotix.Service.RoomService;
import com.example.cydomotix.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@EnableMethodSecurity(prePostEnabled = true) // Pour utiliser les annotations @PreAuthorize
@Controller
@RequestMapping("/object")
public class ObjectViewController {

    @Autowired
    private ConnectedObjectService connectedObjectService;

    @Autowired
    private ObjectTypeService objectTypeService;

    @Autowired
    private ObjectAttributeService objectAttributeService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public String viewObjectDetails(@PathVariable Integer id, Model model) {
        ConnectedObject connectedObject = connectedObjectService.getConnectedObjectById(id);
        if (connectedObject == null) {
            return "redirect:/error"; // Rediriger à la page d'erreur si l'objet est introuvable
        }

        // Objet trouvé, l'ajouter à la vue
        model.addAttribute("connectedObject", connectedObject);
        model.addAttribute("rooms", roomService.getAllRooms());


        // Récupère la session authentifiée en cours (dans l'optique d'ajouter des points lors de la visite d'un objet)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
            // Récupérer le pseudo de l'utilisateur authentifié, dans le contexte du User de Spring Security (qui ne stocke que mdp + login)
            String username = authentication.getName();

            // Récupérer l'entité complète User de la BDD
            Optional<User> userOptional = userService.getByUsername(username);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                //L'utilisateur est connecté. Ajout des points
                userService.updatePoints(user, 2);
            }
        }


        return "object-details";
    }

    @PostMapping("/{id}/update")
    @PreAuthorize("hasRole('ADMIN')") // Restreindre cette requête au rôle ADMIN
    public String updateConnectedObject(@PathVariable Integer id, @ModelAttribute("connectedObject") ConnectedObject updatedObject, RedirectAttributes redirectAttributes) {

        ConnectedObject existingObject = connectedObjectService.getConnectedObjectById(id);
        if (existingObject == null) {
            return "redirect:/error"; // If object not found
        }

        // S'assurer que le nom n'est pas déjà pris si il est modifié, sauf si il reste inchangé
        boolean nameExists = connectedObjectService.objectExists(updatedObject.getName());
        if (nameExists && !existingObject.getName().equals(updatedObject.getName())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Un objet connecté avec ce nom existe déjà.");
            return "redirect:/object/"+id;
        }

        connectedObjectService.update(id, updatedObject);  // Sauvegarder ConnectedObject et ses attributs en BDD
        redirectAttributes.addFlashAttribute("successMessage", "Objet connecté modifié avec succès !");
        return "redirect:/object/"+id;
    }

    /**
     * Supprime un objet connecté de la BDD en récupérant la requête via le bouton Supprimer de la page
     * @param id Id de l'objet à supprimer passé dynamiquement par l'URL
     * @return "redirect:/object/"+id -- La vue html mise à jour
     */
    @GetMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')") // Restreindre cette requête au rôle ADMIN
    public String deleteConnectedObject(@PathVariable("id") Integer id) {
        connectedObjectService.deleteConnectedObject(id);
        return "redirect:/visualisation"; // Recharge la page avec la nouvelle liste
    }

    /**
     * Change le status (activé/désactivé) d'un objet connecté de la BDD en récupérant la requête via le bouton Activer/Désactiver de la page
     * @param id Id de l'objet passé dynamiquement par l'URL
     * @return "redirect:/admin/connected-object" -- La vue html mise à jour
     */
    @GetMapping("/{id}/status")
    public String switchObjectStatus(@PathVariable("id") Integer id) {
        connectedObjectService.switchStatus(id);
        return "redirect:/object/"+id; // Recharge la page avec les informations mises à jour
    }
}
