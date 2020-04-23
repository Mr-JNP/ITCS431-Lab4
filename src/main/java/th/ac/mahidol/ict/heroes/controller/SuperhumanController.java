package th.ac.mahidol.ict.heroes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import th.ac.mahidol.ict.heroes.repository.SuperhumanRepository;
import th.ac.mahidol.ict.heroes.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class SuperhumanController {
    @Autowired
    private SuperhumanRepository superhumanRepository;

    @PostMapping("/add") // Map ONLY GET Requests
    public @ResponseBody
    String addNewHero (
            @RequestParam int id
            , @RequestParam String name
            , @RequestParam(required=false) String realname
            , @RequestParam(required=false) String origin
            , @RequestParam String imageURL
            , @RequestParam String superpower
            , @RequestParam String type
            , @RequestParam(required=false) String weapons
            , @RequestParam(required=false) String friends){
        if (type.equals("hero")) {
            Hero h = new Hero();
            h.setId(id);
            h.setName(name);
            h.setRealname(realname);
            h.setSuperpower(superpower);
            h.setImageURL(imageURL);
            if (weapons != null) {
                String[] weaponArray = weapons.split(";");
                List<Weapon> weaponList = new ArrayList<>();
                for (String w : weaponArray) {
                    String[] warray = w.split(",");
                    weaponList.add(new Weapon(warray[0], warray[1],
                            h));
                }
                h.setWeapons(weaponList);
            }
            if (friends != null) {
                String[] friendArray = friends.split(";");
                List<Human> friendList = new ArrayList<>();
                for (String f: friendArray) {
                    String[] farray = f.split(",");
                    Human friend = new
                            Human(Integer.valueOf(farray[0]), farray[1]);
                    friend.addFriend(h);
                    friendList.add(friend);
                }
                h.setHumanFriends(friendList);
            }
            superhumanRepository.save(h);
            return "Saved: " + h;
        } else if (type.equals("villain")) {
            Villain v = new Villain();
            v.setId(id);
            v.setName(name);
            v.setOrigin(origin);
            v.setSuperpower(superpower);
            v.setImageURL(imageURL);
            if (weapons != null) {
                String[] weaponArray = weapons.split(";");
                List<Weapon> weaponList = new ArrayList<>();
                for (String w : weaponArray) {
                    String[] warray = w.split(",");
                    weaponList.add(new Weapon(warray[0], warray[1],
                            v));
                }
                v.setWeapons(weaponList);
            }
            superhumanRepository.save(v);
            return "Saved: " + v;
        }
        return "Error: wrong superhuman type";
    }

    @GetMapping("/heroes")
    public @ResponseBody
    Iterable<Superhuman> getAllHeroes() {

        return superhumanRepository.findAll();
    }

    @GetMapping("/heroesById")
    public @ResponseBody
    Optional<Superhuman> getHeroById(@RequestParam int id) {

        return superhumanRepository.findById(id);
    }

    @PutMapping("/updateById")
    public @ResponseBody
    String updateHeroById (@RequestParam int id,
                           @RequestParam(required=false) String name,
                           @RequestParam(required=false) String superpower,
                           @RequestParam(required=false) String imageURL,
                           @RequestParam(required=false) String weapons,
                           @RequestParam(required=false) String friends) {

        Optional<Superhuman> superhuman = superhumanRepository.findById(id);
        if(superhuman.isPresent()) {

            Superhuman s = superhuman.get();

            if (name != null) {
                s.setName(name);
            }
            if (superpower != null) {
                s.setSuperpower(superpower);
            }
            if (imageURL != null) {
                s.setImageURL(imageURL);
            }
            if (weapons != null) {
                String[] weaponArray = weapons.split(";");
                List<Weapon> weaponList = new ArrayList<>();
                for (String w : weaponArray) {
                    String[] warray = w.split(",");
                    weaponList.add(new Weapon(warray[0], warray[1],s));
                }
                s.setWeapons(weaponList);
            }
            if (friends != null) {
                String[] friendArray = friends.split(";");
                List<Human> friendList = new ArrayList<>();
                for (String f: friendArray) {
                    String[] farray = f.split(",");
                    Human friend = new
                            Human(Integer.valueOf(farray[0]), farray[1]);
                    friend.addFriend(s);
                    friendList.add(friend);
                }
                s.setHumanFriends(friendList);
            }
            superhumanRepository.save(s);
            return "Saved: " + s;
        }
        return "Hero not found";
    }

    @GetMapping("/deleteById")
    public @ResponseBody
    String deleteById(@RequestParam int id) {

        Optional<Superhuman> superhuman = superhumanRepository.findById(id);
        if(superhuman.isPresent()) {
            Superhuman s = superhuman.get();
            superhumanRepository.deleteById(id);
            return "Removed hero: " + s;
        }
        return "Hero not found";
    }

    @GetMapping("/clean")
    public @ResponseBody
    String clearHeroes() {
        superhumanRepository.deleteAll();
        return "Removed all records";
    }
}
