package com.example.cydomotix.Controller;

import com.example.cydomotix.Model.Objects.ConnectedObject;
import com.example.cydomotix.Model.Objects.Connectivity;
import com.example.cydomotix.Model.Objects.Mode;
import com.example.cydomotix.Model.Objects.ObjectAttribute;
import com.example.cydomotix.Service.Objects.ConnectedObjectService;
import com.example.cydomotix.Service.Objects.ObjectAttributeService;
import com.example.cydomotix.Service.Objects.ObjectTypeService;
import com.example.cydomotix.Service.RoomService;
import com.example.cydomotix.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/visualization")
public class VisualizationController {

    @Autowired
    ConnectedObjectService connectedObjectService;

    @Autowired
    ObjectTypeService objectTypeService;

    @Autowired
    ObjectAttributeService objectAttributeService;

    @Autowired
    RoomService roomService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String visualization(@RequestParam(required = false) String keyword,
                                @RequestParam(required = false) Integer objectTypeId,
                                @RequestParam(required = false) Integer roomId,
                                @RequestParam(required = false) String brand,
                                @RequestParam(required = false) Mode mode,
                                @RequestParam(required = false) Connectivity connectivity,
                                Model model){

        List<ConnectedObject> results = connectedObjectService.searchObjects(keyword, objectTypeId, roomId, brand, mode, connectivity);

        // Liste des objets connectés trouvés
        model.addAttribute("connectedObjects", results);

        // Liste des types enregistrés pour peupler le menu déroulant des types à sélectionner
        model.addAttribute("objectTypes", objectTypeService.getAllObjectTypes());

        // Liste des pièces enregistrées pour peupler le menu déroulant des pièces à sélectionner
        model.addAttribute("rooms", roomService.getAllRooms());

        // Filtres à récupérer avec requête GET
        model.addAttribute("keyword", keyword);
        model.addAttribute("mode", mode);
        model.addAttribute("connectivity", connectivity);
        model.addAttribute("brand", brand);
        model.addAttribute("objectTypeId", objectTypeId);
        model.addAttribute("roomId", roomId);

        return "visualization";
    }

    /**
     * Récupère tous les attributs (champs) d'objets liés à un type, pour les afficher dynamiquement lors de la sélection d'un type d'objet.
     * On envoie le type d'objet voulu via une requête GET avec l'id du type spécifié dans l'url.
     * @param typeId L'id du type d'objet récupéré dans l'url
     * @return Liste des attributs liés au type sélectionné (ObjectAttribute)
     */
    @GetMapping("/attributes")
    @ResponseBody
    // doit renvoyer les données sous forme JSON directement dans la réponse HTTP et non dans un modèle thymeleaf
    public List<ObjectAttribute> getAttributesByType(@RequestParam Integer typeId) {
        return objectAttributeService.getAttributesForObjectType(objectTypeService.getObjectTypeById(typeId));
    }

}
