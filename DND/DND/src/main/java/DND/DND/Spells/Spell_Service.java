package DND.DND.Spells;

import DND.DND.Characters.Character;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Spell_Service
{
    @Autowired
    private Spells_Repository spells_repository;

    public void saveSpells(Character character, String spellJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            // Közvetlenül a gyökérszintű tömb beolvasása
            JsonNode spellsArray = objectMapper.readTree(spellJson);

            for (JsonNode spellNode : spellsArray) {
                String spellName = spellNode.path("spell_name").asText();
                String castingAction = spellNode.path("casting_action").asText();
                String rangeArea = spellNode.path("range_area").asText();
                String spellSchool = spellNode.path("spell_school").asText();
                String spellLevel = spellNode.path("spell_level").asText();
                String description = spellNode.path("spell_description").asText();
                String duration = spellNode.path("spell_duration").asText();
//                System.out.println(spellName);
//                System.out.println(castingAction);
//                System.out.println(rangeArea);
//                System.out.println(spellSchool);
//                System.out.println(spellLevel);
//                System.out.println(description);
//                System.out.println(duration);
//                System.out.println("-----------------------------------------");
                Spells spell = new Spells();
                spell.setName(character.getName());
                spell.setEmail(character.getUser());
                spell.setSpell_name(spellName);
                spell.setCasting_action(castingAction);
                spell.setRange_area(rangeArea);
                spell.setSpell_school(spellSchool);
                spell.setSpell_level(spellLevel);
                spell.setSpell_description(description);
                spell.setSpell_duration(duration);
                spells_repository.save(spell);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
