package com.example.cydomotix.Controller.Objects;

import com.example.cydomotix.Model.Administration.DeletionTargetType;
import com.example.cydomotix.Model.Objects.ConnectedObject;
import com.example.cydomotix.Model.Objects.ObjectAttribute;
import com.example.cydomotix.Model.Objects.ObjectType;
import com.example.cydomotix.Model.Objects.ValueType;
import com.example.cydomotix.Model.Users.User;
import com.example.cydomotix.Service.DeletionRequestService;
import com.example.cydomotix.Service.Objects.AttributeValueService;
import com.example.cydomotix.Service.Objects.ConnectedObjectService;
import com.example.cydomotix.Service.Objects.ObjectAttributeService;
import com.example.cydomotix.Service.Objects.ObjectTypeService;
import com.example.cydomotix.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@RequestMapping("/gestion/object-type") // Toutes les méthodes de cette classe seront relative à une URL commençant par ceci (pas besoin d'être un vrai dossier)
@Controller
public class ObjectTypeController {

    @Autowired
    private ObjectTypeService objectTypeService;

    @Autowired
    private ConnectedObjectService connectedObjectService;
    @Autowired
    private AttributeValueService attributeValueService;
    @Autowired
    private ObjectAttributeService objectAttributeService;
    @Autowired
    private UserService userService;
    @Autowired
    private DeletionRequestService deletionRequestService;

    /**
     * Afficher la page avec le formulaire pour créer un nouveau type objet (ObjectType).
     * Initialise les objets java à récupérer via le formulaire.
     * Affiche la liste des types déjà existants.
     * @param model La vue html
     * @return "gestion/connectedobj" -- La page html à afficher
     */
    @GetMapping
    public String objectTypeForm(Model model) {
        ObjectType objectType = new ObjectType();
        objectType.setAttributes(new ArrayList<>());

        // Liste des types déjà enregistrés
        model.addAttribute("objectTypes", objectTypeService.getAllObjectTypes());

        // Pour le nouveau type d'objet à enregistrer avec le formulaire
        model.addAttribute("objectType", objectType);
        model.addAttribute("attributes", objectType.getAttributes()); // sa liste d'attributs associés


        return "gestion/objtypes";  // Vue pour le formulaire de création
    }

    /**
     * Récupère la requête du formulaire pour envoyer un nouveau type d'objet et ses attributs à la BDD.
     * Vérifie s'il y a des erreurs de nom déjà pris avant de faire la communication avec la BDD.
     * @param objectType Informations du type d'objet envoyé par le formulaire
     * @param model Vue html
     * @param bindingResult Contient les résultats de validation de l'objet envoyé par le formulaire et vérifie les erreurs
     * @param redirectAttributes Pour ajouter des attributs dans un redirect (rediriger vers une nouvelle page avec information de succès)
     * @return "redirect:/gestion/object-type" -- La vue html mise à jour
     */
    @PostMapping("/create")
    public String createObjectType(@ModelAttribute("objectType") ObjectType objectType, Principal principal, Model model, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

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

            return "gestion/objtypes";  // Retourne à la page du formulaire avec les erreurs
        }

        // Associe les attributs au type d'objet
        for (ObjectAttribute attribute : objectType.getAttributes()) {
            attribute.setObjectType(objectType);
        }

        objectTypeService.save(objectType, principal.getName());  // Sauvegarder le type d'objet et ses attributs + logger l'action utilisateur
        redirectAttributes.addFlashAttribute("successMessage", "Type d'objet créé avec succès !");
        return "redirect:/gestion/object-type";
    }

    /**
     * Supprime un Type d'objet de la BDD en récupérant la requête via le bouton Supprimer de la page
     * @param id Id du Type d'objet à supprimer passé dynamiquement par l'URL
     * @return "redirect:/gestion/object-type" -- La vue html mise à jour
     */
    @GetMapping("/delete/{id}")
    public String deleteObjectType(@PathVariable("id") Integer id, Principal principal) {

        List<ConnectedObject> connectedObjectsList = connectedObjectService.getConnectedObjectsByObjectTypeId(id);
        for (ConnectedObject obj : connectedObjectsList) {
            // Supprimer d'abord les AttributeValue liés aux ConnectedObjects du type à supprimer
            attributeValueService.deleteByConnectedObjectId(obj.getId());

            // Supprimer les ConnectedObjects du type à supprimer
            connectedObjectService.deleteConnectedObject(obj.getId(), principal.getName());
        }

        // Supprimer les ObjectAttributes liés au type à supprimer
        objectAttributeService.deleteByObjectTypeId(id);

        // Supprimer le type
        objectTypeService.deleteObjectType(id, principal.getName());
        return "redirect:/gestion/object-type"; // Recharge la page avec la nouvelle liste
    }

    @GetMapping("/valueTypes")
    @ResponseBody
    public List<Map<String, String>> getValueTypes() {
        // Créer une liste d'objets avec le vrai nom et le displayName des valeurs d'enum ValueType
        return Arrays.stream(ValueType.values())
                .map(valueType -> Map.of(
                        "name", valueType.name(),  // Nom de l'enum
                        "displayName", valueType.getDisplayName()  // displayName de l'enum
                ))
                .collect(Collectors.toList());
    }


    @PostMapping("/{id}/request-deletion")
    public String requestDeletion(@PathVariable Integer id, @RequestParam String reason, Principal principal) {
        // Récupérer l'utilisateur de la session actuelle
        Optional<User> user = userService.getByUsername(principal.getName());
        if (user.isPresent()) {
            deletionRequestService.submitRequest(id, DeletionTargetType.OBJECT_TYPE, reason, user.get());
            return "redirect:/gestion/object-type";
        }
        return "redirect:/error";
    }
}

