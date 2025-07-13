package DND.DND.saving_throws;

import DND.DND.Characters.CharacterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import DND.DND.Characters.Character;

import java.util.List;


@Service
public class Saving_throws_service
{
    @Autowired
    Saving_throws_repository saving_throws_repository;

    public Saving_throws save_saving_throws(Character character)
    {
        Saving_throws lista = new Saving_throws();
        lista.setName(character.getName());
        lista.setEmail(character.getUser());

        return saving_throws_repository.save(lista);
    }

    public List<Saving_throws> get_saving_throws(String name, String email)
    {
        return saving_throws_repository.findSavingThrowsByEmailAndName(name, email);
    }
}
