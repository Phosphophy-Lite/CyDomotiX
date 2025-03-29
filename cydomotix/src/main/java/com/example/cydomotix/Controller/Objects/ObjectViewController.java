package com.example.cydomotix.Controller.Objects;

import com.example.cydomotix.Model.Objects.AttributeValue;
import com.example.cydomotix.Model.Objects.ConnectedObject;
import com.example.cydomotix.Model.Objects.ObjectAttribute;
import com.example.cydomotix.Service.Objects.ConnectedObjectService;
import com.example.cydomotix.Service.Objects.ObjectAttributeService;
import com.example.cydomotix.Service.Objects.ObjectTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/object")
public class ObjectViewController {

    @Autowired
    ConnectedObjectService connectedObjectService;

    @Autowired
    ObjectTypeService objectTypeService;

    @Autowired
    ObjectAttributeService objectAttributeService;

    @GetMapping("/{id}")
    public String viewObjectDetails(@PathVariable Integer id, Model model) {
        ConnectedObject connectedObject = connectedObjectService.getConnectedObjectById(id);
        if (connectedObject == null) {
            return "redirect:/error"; // Rediriger à la page d'erreur si l'objet est introuvable
        }

        // Objet trouvé, l'ajouter à la vue
        model.addAttribute("connectedObject", connectedObject);

        return "object-details";
    }

    @PostMapping("/{id}/update")
    public String createConnectedObject(@PathVariable Integer id, @ModelAttribute("connectedObject") ConnectedObject updatedObject, RedirectAttributes redirectAttributes) {

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
}
