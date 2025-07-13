package DND.DND.Characters;

import DND.DND.Character_Notes.Characters_Notes_Repository;
import DND.DND.Currency.Currency_Repository;
import DND.DND.Shields.Shields_Repository;
import DND.DND.Skills.SkillsRepository;
import DND.DND.Spells.Spells_Repository;
import DND.DND.Weapons.Weapons_Repository;
import DND.DND.armor.Armors_Repository;
import DND.DND.features.Features_Repository;
import DND.DND.saving_throws.Saving_throws_repository;
import DND.DND.user.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class CharacterService {

    @Autowired
    private CharacterRepository characterRepository;

    @Autowired
    private SkillsRepository skillsRepository;

    @Autowired
    private Saving_throws_repository saving_throws_repository;

    @Autowired
    private Weapons_Repository weapons_repository;

    @Autowired
    private Features_Repository features_repository;

    @Autowired
    private Armors_Repository armors_repository;

    @Autowired
    private Shields_Repository shields_repository;

    @Autowired
    private Characters_Notes_Repository characters_notes_repository;

    @Autowired
    private Currency_Repository currency_repository;

    @Autowired
    private Spells_Repository spells_repository;

    public Character saveCharacter(CharacterDto characterDto) {
        Character character = new Character();
        character.setName(characterDto.getName());
        character.setLevel(characterDto.getLevel());
        character.setRace(characterDto.getRace());
        character.setKlass(characterDto.getKlass());
        character.setBackground(characterDto.getBackground());
        character.setAlignment(characterDto.getAlignment());
        character.setStrength(characterDto.getStrength());
        character.setDexterity(characterDto.getDexterity());
        character.setConstitution(characterDto.getConstitution());
        character.setIntelligence(characterDto.getIntelligence());
        character.setWisdom(characterDto.getWisdom());
        character.setCharisma(characterDto.getCharisma());
        character.setUser(characterDto.getUser());
        character.setCharisma_b(0);
        character.setConstitution_b(0);
        character.setArmor_class(0);
        character.setDeity(0);
        character.setDexterity_b(0);
        character.setHit_dice(0);
        character.setHp(characterDto.getHp());
        character.setInitiative(0);
        character.setInitiative(0);
        character.setIntelligence_b(0);
        character.setProficiency_bonus(0);
        character.setSpeed(0);
        character.setStrength_b(0);
        character.setWisdom_b(0);
        character.setXp(0);
        character.setImage_URL(characterDto.getImage_URL());

        return characterRepository.save(character);
    }

    public List<Character> getCharactersByEmail(String email) {
        return characterRepository.findCharactersByEmail(email);
    }

    @Transactional
    // Törli a karaktert az id alapján
    public void deleteCharacterById(Long id) {
        // Megkeressük a karaktert az ID alapján
        Optional<Character> characterOptional = characterRepository.findById(id);

        if (characterOptional.isPresent()) {
            Character character = characterOptional.get();
            String name = character.getName();
            String email = character.getUser(); // A karakterhez tartozó email

            // 1️⃣ Először töröljük a karakterhez tartozó skilleket
            skillsRepository.deleteSkillByNameAndEmail(name, email);

            saving_throws_repository.deleteSavingThrowByNameAndEmail(name, email);

            weapons_repository.deleteWeaponByNameAndEmail(name, email);
            armors_repository.deleteArmorsByNameAndEmail(name, email);
            shields_repository.deleteShieldsByNameAndEmail(name, email);
            features_repository.deleteFeaturesByNameAndEmail(name, email);
            characters_notes_repository.deleteCharacters_NotesByNameAndEmail(name, email);
            currency_repository.deleteCurrenciesByNameAndEmail(name, email);
            spells_repository.deleteSpellsByNameAndEmail(name, email);

            // 2️⃣ Majd töröljük a karaktert
            characterRepository.deleteById(id);
        }
    }

    public Character updateCharacter(Long id, CharacterDto characterDto) {
        // Lekérjük az adatbázisból az adott karaktert
        Character character = characterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Character not found"));

        // Frissítjük a meglévő karakter adatait a DTO alapján
        character.setName(characterDto.getName());
        character.setLevel(characterDto.getLevel());
        character.setRace(characterDto.getRace());
        character.setKlass(characterDto.getKlass());
        character.setBackground(characterDto.getBackground());
        character.setAlignment(characterDto.getAlignment());
        character.setStrength(characterDto.getStrength());
        character.setDexterity(characterDto.getDexterity());
        character.setConstitution(characterDto.getConstitution());
        character.setIntelligence(characterDto.getIntelligence());
        character.setWisdom(characterDto.getWisdom());
        character.setCharisma_b(0);
        character.setConstitution_b(0);
        character.setArmor_class(0);
        character.setDeity(0);
        character.setDexterity_b(0);
        character.setHit_dice(0);
        character.setHp(characterDto.getHp());
        character.setInitiative(0);
        character.setInitiative(0);
        character.setIntelligence_b(0);
        character.setProficiency_bonus(0);
        character.setSpeed(0);
        character.setStrength_b(0);
        character.setWisdom_b(0);
        character.setXp(0);
        character.setImage_URL(characterDto.getImage_URL());

        // Elmentjük a módosított karaktert az adatbázisba
        return characterRepository.save(character);
    }
}
