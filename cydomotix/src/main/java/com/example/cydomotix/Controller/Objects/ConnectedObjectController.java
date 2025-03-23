package com.example.cydomotix.Controller.Objects;

import com.example.cydomotix.Model.Objects.AttributeValue;
import com.example.cydomotix.Model.Objects.ConnectedObject;
import com.example.cydomotix.Model.Objects.ObjectAttribute;
import com.example.cydomotix.Model.Objects.ObjectType;
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

    /**
     * Afficher la page avec le formulaire pour créer un nouvel objet connecté.
     * Initialise les objets java à récupérer via le formulaire.
     * @param model La vue html
     * @return "admin/connectedobj" -- La page html à afficher
     */
    @GetMapping
    public String connectedObjectForm(Model model) {
        ConnectedObject connectedObject = new ConnectedObject();
        connectedObject.setAttributeValueList(new ArrayList<>());

        // Liste des objets connectés déjà enregistrés
        model.addAttribute("connectedObjects", connectedObjectService.getAllConnectedObjects());

        // Liste des types enregistrés pour peupler le menu déroulant des types à sélectionner
        model.addAttribute("objectTypes", objectTypeService.getAllObjectTypes());

        // Pour le nouvel objet à enregistrer avec le formulaire
        model.addAttribute("connectedObject", connectedObject);
        model.addAttribute("attributeValues", connectedObject.getAttributeValueList()); // sa liste de valeurs d'attributs associés

        return "admin/connectedobj";  // Retourne le vrai chemin de la vue pour le formulaire de création
    }

    /**
     * Récupère tous les attributs (champs) d'objets liés à un type, pour les afficher dynamiquement lors de la sélection d'un type d'objet.
     * On envoie le type d'objet voulu via une requête GET avec l'id du type spécifié dans l'url.
     * @param typeId L'id du type d'objet récupéré dans l'url
     * @return Liste des attributs liés au type sélectionné (ObjectAttribute)
     */
    @GetMapping("/attributes")
    @ResponseBody // doit renvoyer les données sous forme JSON directement dans la réponse HTTP et non dans un modèle thymeleaf
    public List<ObjectAttribute> getAttributesByType(@RequestParam Integer typeId) {
        return objectAttributeService.getAttributesForObjectType(objectTypeService.getObjectTypeById(typeId));
    }

    /**
     * Récupère la requête du formulaire pour envoyer un nouvel objet connecté à la BDD.
     * Vérifie s'il y a des erreurs de nom déjà pris avant de faire la communication avec la BDD.
     * @param connectedObject Informations de l'objet connecté envoyé par le formulaire
     * @param model Vue html
     * @param bindingResult Contient les résultats de validation de l'objet envoyé par le formulaire et vérifie les erreurs
     * @param redirectAttributes Pour ajouter des attributs dans un redirect (rediriger vers une nouvelle page avec information de succès)
     * @return "redirect:/admin/connected-object" -- La vue html mise à jour
     */
    @PostMapping("/add")
    public String createConnectedObject(@ModelAttribute("connectedObject") ConnectedObject connectedObject, Model model, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        // Vérifie que l'ObjectType sélectionné et envoyé via le formulaire existe bien en BDD
        ObjectType existingType = objectTypeService.getObjectTypeById(connectedObject.getObjectType().getId());
        connectedObject.setObjectType(existingType); // On remplace l'instance transiente par une instance persistante

        // Vérifie si le nom existe déjà dans la BDD, si oui, renvoyer un message d'erreur à la vue de l'utilisateur
        if (connectedObjectService.objectExists(connectedObject.getName())) {
            bindingResult.rejectValue("name", "error.connectedObject", "Un objet connecté avec ce nom existe déjà.");
        }

        // Vérifie s'il y a des erreurs
        if (bindingResult.hasErrors()) {

            // Liste des objets connectés déjà enregistrés
            model.addAttribute("connectedObjects", connectedObjectService.getAllConnectedObjects());

            // Liste des types enregistrés pour peupler le menu déroulant des types à sélectionner
            model.addAttribute("objectTypes", objectTypeService.getAllObjectTypes());

            // Pour le nouvel objet à enregistrer avec le formulaire
            model.addAttribute("connectedObject", connectedObject);
            model.addAttribute("attributeValues", connectedObject.getAttributeValueList()); // sa liste de valeurs d'attributs associés

            return "admin/connectedobj";  // Retourne à la page du formulaire avec les erreurs
        }

        // Associe les valeurs d'attributs à l'objet connecté et à un ObjectAttribute
        for (AttributeValue attributeValue : connectedObject.getAttributeValueList()) {
            attributeValue.setConnectedObject(connectedObject);

            // Vérifie que l'ObjectAttribute sélectionné et envoyé via le formulaire existe bien en BDD
            ObjectAttribute existingAttribute = objectAttributeService.getObjectAttributeById(attributeValue.getObjectAttribute().getId());
            attributeValue.setObjectAttribute(existingAttribute); // On remplace l'instance transiente par une instance persistante
        }

        connectedObjectService.save(connectedObject);  // Sauvegarder ConnectedObject et ses attributs en BDD
        redirectAttributes.addFlashAttribute("successMessage", "Objet connecté ajouté avec succès !");
        return "redirect:/admin/connected-object";
    }

    /**
     * Supprime un objet connecté de la BDD en récupérant la requête via le bouton Supprimer de la page
     * @param id Id de l'objet à supprimer passé dynamiquement par l'URL
     * @return "redirect:/admin/connected-object" -- La vue html mise à jour
     */
    @GetMapping("/delete/{id}")
    public String deleteObjectType(@PathVariable("id") Integer id) {
        connectedObjectService.deleteConnectedObject(id);
        return "redirect:/admin/connected-object"; // Recharge la page avec la nouvelle liste
    }
}

