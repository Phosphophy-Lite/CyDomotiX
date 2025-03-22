package com.example.cydomotix.Controller.Objects;

import com.example.cydomotix.Model.Objects.ObjectAttribute;
import com.example.cydomotix.Model.Objects.ObjectType;
import com.example.cydomotix.Service.Objects.ObjectTypeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;

@Controller
public class ObjectTypeController {

    @Autowired
    private ObjectTypeService objectTypeService;

    @GetMapping("/admin/objtypes")
    public String objectTypeForm(Model model) {
        ObjectType objectType = new ObjectType();
        objectType.setAttributes(new ArrayList<>());

        // Liste des types déjà enregistrés
        model.addAttribute("objectTypes", objectTypeService.getAllObjectTypes());

        // Pour le nouveau type d'objet à enregistrer avec le formulaire
        model.addAttribute("objectType", objectType);
        model.addAttribute("attributes", objectType.getAttributes()); // sa liste d'attributs associés


        return "admin/objtypes";  // Vue pour le formulaire de création
    }

    @PostMapping("/create")
    public String createObjectType(@ModelAttribute ObjectType objectType, Model model, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        // Vérifie si le nom existe déjà dans la BDD, si oui, renvoyer un message d'erreur à la vue de l'utilisateur
        if (objectTypeService.objectTypeNameExists(objectType.getName())) {
            bindingResult.rejectValue("name", "error.objectType", "Un Type d'objet avec ce nom existe déjà.");
        }

        // Vérifie s'il y a des erreurs
        if (bindingResult.hasErrors()) {
            model.addAttribute("objectTypes", objectTypeService.getAllObjectTypes());

            // Pour le nouveau type d'objet à enregistrer avec le formulaire
            model.addAttribute("objectType", objectType);
            model.addAttribute("attributes", objectType.getAttributes()); // sa liste d'attributs associés

            return "admin/objtypes";  // Retourne à la page du formulaire avec les erreurs
        }

        // Associe les attributs au type d'objet
        for (ObjectAttribute attribute : objectType.getAttributes()) {
            attribute.setObjectType(objectType);
        }

        objectTypeService.save(objectType);  // Save the ObjectType with its attributes
        redirectAttributes.addFlashAttribute("successMessage", "Type d'objet créé avec succès !");
        return "redirect:/admin/objtypes";
    }

    /**
     * Supprimer un Type d'objet de la BDD
     */
    @GetMapping("/delete/{id}")
    public String deleteObjectType(@PathVariable("id") Integer id) {
        objectTypeService.deleteObjectType(id);
        return "redirect:/admin/objtypes"; // Recharge la page avec la nouvelle liste
    }

}
