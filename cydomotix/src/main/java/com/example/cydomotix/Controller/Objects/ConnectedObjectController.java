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

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/connected-object") // Toutes les méthodes de cette classe seront relative à une URL commençant par ceci (pas besoin d'être un vrai dossier)
public class ConnectedObjectController {


    @Autowired
    private ConnectedObjectService connectedObjectService;

    @Autowired
    private ObjectTypeService objectTypeService;

    @Autowired
    private ObjectAttributeService objectAttributeService;

    @GetMapping
    public String connectedObjectForm(Model model) {
        ConnectedObject connectedObject = new ConnectedObject();
        connectedObject.setAttributeValueList(new ArrayList<>());

        // Liste des types enregistrés
        model.addAttribute("objectTypes", objectTypeService.getAllObjectTypes());

        // Pour le nouvel objet à enregistrer avec le formulaire
        model.addAttribute("connectedObject", connectedObject);
        model.addAttribute("attributeValues", connectedObject.getAttributeValueList()); // sa liste de valeurs d'attributs associés


        return "admin/connectedobj";  // Retourne le vrai chemin de la vue pour le formulaire de création
    }

    @GetMapping("/attributes")
    @ResponseBody // doit renvoyer les données sous forme JSON directement dans la réponse HTTP et non dans un modèle thymeleaf
    public List<ObjectAttribute> getAttributesByType(@RequestParam Integer typeId) {
        return objectAttributeService.getAttributesForObjectType(objectTypeService.getObjectTypeById(typeId));

    }

    @PostMapping("/add")
    public String createConnectedObject(@ModelAttribute ConnectedObject connectedObject, Model model, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        System.out.println("Object name : " + connectedObject.getName() + " Type name : " + connectedObject.getObjectType().getName());
        // Vérifie si le nom existe déjà dans la BDD, si oui, renvoyer un message d'erreur à la vue de l'utilisateur
        if (connectedObjectService.objectExists(connectedObject.getName())) {
            bindingResult.rejectValue("name", "error.connectedObject", "Un objet connecté avec ce nom existe déjà.");
        }

        // Vérifie s'il y a des erreurs
        if (bindingResult.hasErrors()) {
            model.addAttribute("objectTypes", objectTypeService.getAllObjectTypes());

            // Pour le nouvel objet à enregistrer avec le formulaire
            model.addAttribute("connectedObject", connectedObject);
            model.addAttribute("attributeValues", connectedObject.getAttributeValueList()); // sa liste de valeurs d'attributs associés

            return "admin/connectedobj";  // Retourne à la page du formulaire avec les erreurs
        }

        // Associe les valeurs d'attributs à l'objet
        for (AttributeValue attributeValue : connectedObject.getAttributeValueList()) {
            attributeValue.setConnectedObject(connectedObject);
        }

        connectedObjectService.save(connectedObject);  // Save the ObjectType with its attributes
        redirectAttributes.addFlashAttribute("successMessage", "Objet connecté ajouté avec succès !");
        return "redirect:/admin/connected-object";
    }

    /**
     * Supprimer un objet connecté de la BDD
     */
    @GetMapping("/delete/{id}")
    public String deleteObjectType(@PathVariable("id") Integer id) {
        connectedObjectService.deleteConnectedObject(id);
        return "redirect:/admin/connected-object"; // Recharge la page avec la nouvelle liste
    }
}
