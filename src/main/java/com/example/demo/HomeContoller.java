package com.example.demo;

import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.Map;

@Controller
public class HomeContoller {

    @Autowired
    PetRepository petRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    CloudinaryConfig cloudc;

    @Autowired
    private UserService userService;

    @RequestMapping("/")
    public String listMessages(Model model) {
        model.addAttribute("pets", petRepository.findAllByOrderById());
        return "list";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationPage(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/register")
    public String processRegistrationPage(@Valid @ModelAttribute("user") User user,
                                          BindingResult result,
                                          Model model) {
        model.addAttribute("user", user);
        if (result.hasErrors()) {
            return "registration";
        } else {
            boolean isUser = userRepository.findById(user.getId()).isPresent();
            if (isUser) {//For Update Registration
                Iterable<Pet> pets = petRepository.findAllByUsers(user);
                for (Pet pet : pets) {
                    petRepository.save(pet);
                    user.getPets().add(pet);
                }
            }
            if(userService.isUser()){
                userService.saveUser(user);
            }
            if(userService.isAdmin()){
                userService.saveAdmin(user);
            }
            model.addAttribute("message", "User Account Successfully Created");
        }
        return "redirect:/login";
    }

    @GetMapping("/addPet")
    public String addMessage(Model model) {
        model.addAttribute("pet", new Pet());
        model.addAttribute("imageLabel", "Upload Image");
        return "petform";
    }

    @PostMapping("/addPet")
    public String processMessage(@Valid @ModelAttribute("pet") Pet pet, BindingResult result,
                                 @RequestParam("file") MultipartFile file, HttpServletRequest request,
                                 @RequestParam("hiddenImgURL") String image,
                                 Authentication authentication, Principal principal, Model model) {
        if (result.hasErrors()) {
            return "petform";
        }
        if (file.isEmpty()) {
            if (!image.isEmpty()) {
                pet.setImage(image);
            }
        } else {
            try {
                Map uploadResult = cloudc.upload(file.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));
                pet.setImage(uploadResult.get("url").toString());
            } catch (IOException e) {
                e.printStackTrace();
                return "redirect:/";
            }
        }
      /*  Boolean isAdmin = request.isUserInRole("ADMIN");
        Boolean isUser = request.isUserInRole("USER");
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();*/
        String username = principal.getName();
        User u = userRepository.findByUsername(username);
        pet.getUsers().add(u);
        petRepository.save(pet);

        u.getPets().add(pet);
        userRepository.save(u);
        return "redirect:/user";
    }

    @RequestMapping("/updatePet/{id}")
    public String updateMessage(@PathVariable("id") long id, Model model) {
        Pet pet = petRepository.findById(id).get();
        model.addAttribute("pet", petRepository.findById(id));
        model.addAttribute("image", pet.getImage());
        if (pet.getImage().isEmpty()) {
            model.addAttribute("imageLabel", "Upload Image");
        } else {
            model.addAttribute("imageLabel", "Upload New Image");
        }
        return "petform";
    }

/*    @RequestMapping("/user")
    public String showUser(Model model,
                           HttpServletRequest request,
                           Authentication authentication,
                           Principal principal) {
        Boolean isAdmin = request.isUserInRole("ADMIN");
        Boolean isUser = request.isUserInRole("USER");
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = principal.getName();
        User user = userRepository.findByUsername(username);

        model.addAttribute("pets", petRepository.findAllByUsers(user));
        return "userpage";
    }*/

    @RequestMapping("/user")
    public String showUser(Model model,
                           HttpServletRequest request,
                           Authentication authentication,
                           Principal principal) {
        /*Boolean isAdmin = request.isUserInRole("ADMIN");
        Boolean isUser = request.isUserInRole("USER");
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();*/

        if (userService.isUser()) {
            String username = principal.getName();//userDetails.getUsername()
            User user = userRepository.findByUsername(username);
            model.addAttribute("pets", petRepository.findAllByUsers(user));
        }
        if (userService.isAdmin()) {
            model.addAttribute("pets", petRepository.findAll());
        }
        return "userpage";
    }

    @RequestMapping("/updateUser")
    public String viewUser(Model model, HttpServletRequest request, Authentication authentication, Principal principal) {
       /* Boolean isAdmin = request.isUserInRole("ADMIN");
        Boolean isUser = request.isUserInRole("USER");
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();*/
        String username = principal.getName();
        model.addAttribute("user", userRepository.findByUsername(username));
        return "registration";
    }

    @RequestMapping("/changeStatus/{id}")
    public String changeStatus(@PathVariable("id") long id) {
        Pet pet = petRepository.findById(id).get();
        if (pet.getStatus().equalsIgnoreCase("Lost")) {
            pet.setStatus("Found");
        } else {
            pet.setStatus("Lost");
        }
        petRepository.save(pet);
        return "redirect:/user";
    }
}
