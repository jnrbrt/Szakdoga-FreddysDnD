package DND.DND.user;

import DND.DND.Character_Notes.Characters_Notes;
import DND.DND.Character_Notes.Characters_Notes_Repository;
import DND.DND.Characters.Character;
import DND.DND.Characters.CharacterService;
import DND.DND.Currency.Currency_Repository;
import DND.DND.Currency.Currency_Service;
import DND.DND.Currency.Currency;
import DND.DND.Shields.Shields;
import DND.DND.Shields.Shields_Repository;
import DND.DND.Skills.Skills;
import DND.DND.Skills.SkillsService;
import DND.DND.Weapons.Weapons;
import DND.DND.Weapons.Weapons_Repository;
import DND.DND.armor.Armors;
import DND.DND.armor.Armors_Repository;
import DND.DND.features.Features;
import DND.DND.features.Features_Repository;
import DND.DND.saving_throws.Saving_throws;
import DND.DND.saving_throws.Saving_throws_service;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UserController
{
    @Autowired
    private UserService userService;

    @Autowired
    private CharacterService characterService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SkillsService skillsService;

    @Autowired
    private Saving_throws_service saving_throws_service;

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

    @GetMapping("/")
    public String mainPage()
    {
        return "main";
    }

    @GetMapping("/registration")
    public String getRegistrationPage(@ModelAttribute("user")UserDto userDto)
    {
        return "register";
    }

    @PostMapping("/registration")
    public String saveUser(@ModelAttribute("user")UserDto userDto, Model model)
    {
        userService.save(userDto);
        model.addAttribute("message", "Registered Succesfully");
        return "login";
    }

    @GetMapping("/login")
    public String login()
    {
        return "login";
    }

    @GetMapping("/characters")
    public String loged_in(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();
        User user = userService.findByEmail(email);
        if (authentication != null) {
            if (user != null) {
                model.addAttribute("username", user.getName());// Felhasználó neve
                String imageurl = userRepository.findUserImageByNameAndEmail(user.getName(), user.getEmail());
                model.addAttribute("imageurl", imageurl);
            } else {
                model.addAttribute("username", "Guest");
            }
        }
        String username = authentication.getName();
        // Karakterek lekérdezése az e-mail alapján
        List<Character> characters = characterService.getCharactersByEmail(username);
        // Karakterek átadása a modelhez
        model.addAttribute("characters", characters);
        //Skillek lekérdezése és átadása a modellnek
        Map<String, List<Skills>> skillsMap = new HashMap<>();
        Map<String, List<Saving_throws>> saving_throws_map = new HashMap<>();
        Map<String, List<Weapons>> weapons_map = new HashMap<>();
        Map<String, Features> features_map = new HashMap<>();
        Map<String, List<Armors>> armors_map = new HashMap<>();
        Map<String, List<Shields>> shields_map = new HashMap<>();
        Map<String, Characters_Notes> characters_notes_map = new HashMap<>();
        Map<String, Currency> currencyMap = new HashMap<>();

        for (Character character : characters) {
            // Skills adat lekérése
            List<Skills> skills = skillsService.getSkills(character.getName(), authentication.getName());
            skillsMap.put(character.getName(), skills);

            // Saving Throws adat lekérése
            List<Saving_throws> saving_throws = saving_throws_service.get_saving_throws(character.getName(), character.getUser());
            saving_throws_map.put(character.getName(), saving_throws);

            // Weapons adat lekérése
            List<Weapons> weapons = weapons_repository.findWeaponsByNameAndEmail(character.getName(), character.getUser());
            weapons_map.put(character.getName(), weapons);

            //Armors adat lekérése
            List<Armors> armors = armors_repository.findArmorsByNameAndEmail(character.getName(), character.getUser());
            armors_map.put(character.getName(), armors);

            //Shields adat lekérése
            List<Shields> shields = shields_repository.findShieldsByNameAndEmail(character.getName(), character.getUser());
            shields_map.put(character.getName(), shields);

            //Features adat lekérése
            Features features = features_repository.findFeaturesByCharacter(character.getName(), character.getUser());
            features_map.put(character.getName(), features);

            Characters_Notes characters_notes = characters_notes_repository.findCharacters_NotesByNameAndEmail(character.getName(), character.getUser());
            characters_notes_map.put(character.getName(), characters_notes);

            Currency currency = currency_repository.findCurrenciesByNameAndEmail(character.getName(), character.getUser());
            currencyMap.put(character.getName(), currency);
        }

        model.addAttribute("skillsMap", skillsMap);
        model.addAttribute("saving_throws_map", saving_throws_map);
        model.addAttribute("weapons_map", weapons_map);
        model.addAttribute("features_map", features_map);
        model.addAttribute("armors_map", armors_map);
        model.addAttribute("shields_map", shields_map);
        model.addAttribute("characters_notes_maps", characters_notes_map);
        model.addAttribute("currency_map", currencyMap);
        return "Characters";
    }

//    @GetMapping("/choose")
//    public String choose_html()
//    {
//        return "choose.html";
//    }

    @GetMapping("/choose") //ÁTIRT DOLOG
    public String choose_html(Model model, Principal principal)
    {
        String email = principal.getName();
        User user = userService.findByEmail(email);
        if (user != null) {
            model.addAttribute("name", user.getName());
        } else {
            model.addAttribute("name", "Felhasználó alapértelmezett");
        }
        return "choose";
    }

    @GetMapping("/back")
    public String back_to_choose()
    {
        return "redirect:/choose";
    }

    @GetMapping("/profile")
    public String showProfile(Model model)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            String email = authentication.getName();
            User user = userService.findByEmail(email);
            if (user != null) {
                model.addAttribute("username", user.getName()); // Felhasználó neve
            } else {
                model.addAttribute("username", "Guest");
            }
        }

        String username = authentication.getName();

        model.addAttribute("email", username);

        return "profile";
    }

    @PostMapping("/profile_image")
    public String image(@RequestParam("imageUrl") String imageurl)
    {Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //System.out.println(imageurl);
        if (authentication != null) {
            String email = authentication.getName();
            User user = userService.findByEmail(email);
            userRepository.updateUserImageByNameAndEmail(imageurl, user.getName(), user.getEmail());
        }
        return "redirect:/choose";
    }

    @GetMapping("/api/profile-info") //TELJESEN UJ
    @ResponseBody
    public Map<String, Object> getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            User user = userService.findByEmail(email);
            Map<String, Object> data = new HashMap<>();
            if (user != null) {
                data.put("username", user.getName());
                data.put("email", user.getEmail());
                // Feltételezem a getter így hívható:
                data.put("imageUrl", user.getImageUrl() != null ? user.getImageUrl() : "/media/profile-icon.png");
            } else {
                data.put("username", "Guest");
                data.put("email", "");
                data.put("imageUrl", "/media/profile-icon.png");
            }
            return data;
        }
        return Collections.emptyMap();
    }


    @GetMapping("/change_password")
    public String change_password()
    {
        return "haha";
    }

    @GetMapping("/campaigns")
    public String campaigns()
    {
        return "campaigns";
    }

    @GetMapping("/room")
    public String room(Model model, Principal principal)
    {
        String email = principal.getName(); // Ez a bejelentkezett felhasználó neve
        User user = userService.findByEmail(email);
        model.addAttribute("username", user.getName());
        return "room"; // vagy ahogy hívod az oldalad
    }
}

