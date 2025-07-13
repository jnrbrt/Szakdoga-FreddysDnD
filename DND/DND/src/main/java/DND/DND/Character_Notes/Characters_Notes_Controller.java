package DND.DND.Character_Notes;

import DND.DND.Characters.Character;
import DND.DND.Characters.CharacterRepository;
import DND.DND.Characters.CharacterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class Characters_Notes_Controller
{
    @Autowired
    private CharacterRepository characterRepository;

    @Autowired
    private Characters_Notes_Service characters_notes_service;

    @PostMapping("/save_inventory")
    public String save_inventory(Long id, String inventory)
    {
        System.out.println(id);
        Character character = characterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Character not found"));
        characters_notes_service.saveInventory(character, inventory);
        System.out.println("Inventory: " + inventory);
        return "redirect:/characters";
    }

    @PostMapping("/save_notes")
    public String save_notes(Long id, String note)
    {
        Character character = characterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Character not found"));
        characters_notes_service.saveNote(character, note);
        System.out.println("Note: " + note);
        return "redirect:/characters";
    }
}
