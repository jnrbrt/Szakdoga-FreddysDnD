package DND.DND.Skills;

import DND.DND.Characters.Character;
import DND.DND.Characters.CharacterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillsService
{
    @Autowired
    SkillsRepository skillsRepository;

    @Autowired
    private CharacterRepository characterRepository;

    public Skills saveSkills(Character character)
    {
        Skills skills = new Skills();
        skills.setName(character.getName());
        skills.setEmail(character.getUser());
        return skillsRepository.save(skills);
    }

    public void updateSkills(Character character) {
        // Megkeressük a karakterhez tartozó skillt a listából
        List<Skills> skillsList = getSkills(character.getName(), character.getUser());
        //System.out.println(skillsList);
        if (!skillsList.isEmpty()) {
            Skills skills = skillsList.get(0);
            skills.setName(character.getName());
            skills.setEmail(character.getUser());
            skillsRepository.save(skills);
        }
    }
    public List<Skills> getSkills(String name, String email)
    {
        return skillsRepository.findCharactersBySkill(name, email);
    }
}
