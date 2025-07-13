package DND.DND.Characters;

import DND.DND.Character_Notes.Characters_Notes;
import DND.DND.Character_Notes.Characters_Notes_Service;
import DND.DND.Currency.Currency_Dto;
import DND.DND.Currency.Currency_Service;
import DND.DND.Shields.Shields;
import DND.DND.Shields.Shields_Service;
import DND.DND.Skills.SkillsService;
import DND.DND.Spells.Spell_Service;
import DND.DND.Weapons.Weapons;
import DND.DND.Weapons.Weapons_Service;
import DND.DND.armor.Armors;
import DND.DND.armor.Armors_Service;
import DND.DND.features.Features;
import DND.DND.features.Features_Dto;
import DND.DND.features.Features_Service;
import DND.DND.saving_throws.Saving_throws_service;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.*;

@Controller
public class CharacterController {

    @Autowired
    private CharacterService characterService;

    @Autowired
    private CharacterRepository characterRepository;

    @Autowired
    private SkillsService skillsService;

    @Autowired
    private Saving_throws_service saving_throws_service;

    @Autowired
    private Weapons_Service weapons_service;

    @Autowired
    private Armors_Service armors_service;

    @Autowired
    private Shields_Service shields_service;

    @Autowired
    private Features_Service features_service;

    @Autowired
    private Characters_Notes_Service characters_notes_service;

    @Autowired
    private Currency_Service currency_service;

    @Autowired
    private Spell_Service spell_service;

    @GetMapping("/create")
    public String showCreateCharacterForm(Model model, Principal principal,  RedirectAttributes redirectAttributes) {
        String username = principal.getName();
        List<Character> characters = characterService.getCharactersByEmail(username);
        if (characters.size() == 3)
        {
            redirectAttributes.addFlashAttribute("characterLimitError", "Nem hozhatsz létre több karaktert! Fizess elő a pro verzióra!");
            return "redirect:/characters";
        }

        // Új karakter létrehozásához DTO
        CharacterDto characterDto = new CharacterDto();
        characterDto.setUser(username);
        // Adatok hozzáadása a modelhez
        model.addAttribute("characterDto", characterDto);

        Features_Dto features_dto = new Features_Dto();
        model.addAttribute("features_dto", features_dto);

        Currency_Dto currency_dto = new Currency_Dto();
        model.addAttribute("currency_dto", currency_dto);
        return "Character_Create";
    }

    // Karakter mentése
    @PostMapping("/create")
    public String createCharacter(@ModelAttribute("characterDto") CharacterDto characterDto, Features_Dto features_dto, Currency_Dto currency_dto, @RequestParam String equipment, String story_string, String inventory_string, String spells, Model model) {
        List<Character> characters = characterService.getCharactersByEmail(characterDto.getUser());
        // Ellenőrizzük, hogy van-e már ugyanilyen nevű karakter
        for (Character character : characters) {
            if (character.getName().equals(characterDto.getName())) {
                // Ha találunk már ilyen nevű karaktert, hibaüzenetet adunk hozzá
                model.addAttribute("errorMessage", "A karakter név már létezik!");
                return "Character_Create";  // Visszatérünk a karakter létrehozó oldalra
            }
        }
        Character savedCharacter = characterService.saveCharacter(characterDto);
        skillsService.saveSkills(savedCharacter);
        saving_throws_service.save_saving_throws(savedCharacter);
        features_service.saveFeatures(features_dto, characterDto);
        //System.out.println("A create során kapott json:\t" + equipment);
        weapons_service.saveWeapons(savedCharacter, equipment);
        armors_service.saveArmors(savedCharacter, equipment);
        shields_service.saveShields(savedCharacter, equipment);
        //System.out.println("Story: " + story_string);
        characters_notes_service.saveStoryAndInventory(savedCharacter, story_string, inventory_string);
        currency_service.saveCurrency(savedCharacter, currency_dto);
//        System.out.println(spells);
        spell_service.saveSpells(savedCharacter, spells);
        return "redirect:/characters";
    }

    @GetMapping("/edit/{id}")
    public String editCharacter(@PathVariable("id") Long id, Model model) throws JsonProcessingException {
        // Keresés a karakter ID alapján
        Character character = characterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Character not found"));

        // DTO-ba másoljuk az adatokat
        CharacterDto characterDto = new CharacterDto();
        characterDto.setName(character.getName());
        characterDto.setLevel(character.getLevel());
        characterDto.setRace(character.getRace());
        characterDto.setKlass(character.getKlass());
        characterDto.setBackground(character.getBackground());
        characterDto.setAlignment(character.getAlignment());
        characterDto.setStrength(character.getStrength());
        characterDto.setDexterity(character.getDexterity());
        characterDto.setConstitution(character.getConstitution());
        characterDto.setIntelligence(character.getIntelligence());
        characterDto.setWisdom(character.getWisdom());
        characterDto.setHp(character.getHp());
        characterDto.setImage_URL(character.getImage_URL());

        // Az adatokat átadjuk a modellnek
        model.addAttribute("characterDto", characterDto);


        //Weapons
        List<Weapons> weaponsList = weapons_service.getWeapons(character.getName(), character.getUser());
        List<Armors> armorsList = armors_service.getArmors(character.getName(), character.getUser());
        List<Shields> shieldsList = shields_service.getShields(character.getName(), character.getUser());

        //Átadjuk a fornendnek a már meglévő adatokat
        Map<String, Object> equipments = new HashMap<>();
        equipments.put("weapons", weaponsList);
        equipments.put("armors", armorsList);
        equipments.put("shields", shieldsList);
        model.addAttribute("equipments", equipments);

        Characters_Notes characters_notes = characters_notes_service.getCharacters_Notes(character.getName(), character.getUser());
        model.addAttribute("characters_notes_story", characters_notes.getStory());

        Features features = features_service.getFeatures(character.getName(), character.getUser());
        model.addAttribute("feature", features);
        return "edit";
    }

    @PostMapping("/edit/{id}")
    public String updateCharacter(@PathVariable("id") Long id, @ModelAttribute CharacterDto characterDto, Features_Dto features_dto, String equipmentJson, String story) {
       Character character =  characterService.updateCharacter(id, characterDto);
       skillsService.updateSkills(character);
       System.out.println("Az edit során kapott json:\t" + equipmentJson);
       weapons_service.updateWeapons(character, equipmentJson);
       armors_service.updateArmors(character, equipmentJson);
       shields_service.updateShields(character, equipmentJson);
       System.out.println("A kapott stpry string: " + story);
       characters_notes_service.updateStory(character, story);
       features_service.updateFeatures(features_dto, character);
       return "redirect:/characters";
    }

    // Karakter törlésének kezelése
    @PostMapping("/deleteCharacter")
    public String deleteCharacter(@RequestParam("characterId") Long characterId, Model model) {
        // Karakter törlése
        characterService.deleteCharacterById(characterId);

        // Üzenet megjelenítése a törlés után
        model.addAttribute("message", "Character deleted successfully");
        // Visszairányítás a karakterek listájára
        return "redirect:/characters";
    }
}
