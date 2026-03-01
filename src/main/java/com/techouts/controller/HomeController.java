package com.techouts.controller;

import com.techouts.model.Product;
import com.techouts.model.User;
import com.techouts.service.CartService;
import com.techouts.service.OrdersService;
import com.techouts.service.ProductService;
import com.techouts.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {

    @Autowired
    ProductService productService;
    @Autowired
    UserService userService;
    @GetMapping({"/","/index"})
    public String home(Model model) {

        List<Product> products = productService.findAll();
        model.addAttribute("products", products);

        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }


    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user,
                               RedirectAttributes redirectAttributes) {

        Optional<User> existingUser = userService.findByEmail(user.getEmail());
        Optional<User> userByPhone  = userService.findByPhone(user.getPhone());

        if (existingUser.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Email already exists");
            return "redirect:/register";
        }

        if (userByPhone.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Phone number already exists");
            return "redirect:/register";
        }

        String rawPassword = user.getPassword();

        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(User.UserRole.ROLE_CUSTOMER);
        userService.save(user);


      /*  Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        rawPassword
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);*/

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(user.getEmail(), rawPassword);
        Authentication auth = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(auth);


        return "redirect:/products";
    }

    @Autowired
    CartService cartService;
    @Autowired
    OrdersService ordersService;
    @GetMapping("/profile")
    public String profile(Model model, Principal principal) {


        if (principal == null) {
            return "redirect:/login";
        }
        String email = principal.getName();
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        int cartCount = cartService.getCartProductCount(user);
        int orderCount = ordersService.getOrderCount(user);

        model.addAttribute("user", user);
        model.addAttribute("cartCount", cartCount);
        model.addAttribute("orderCount", orderCount);

        return "profile";
    }

    @PostMapping("/updateProfile")
    public String updateProfile(@RequestParam("name") String name,
                                @RequestParam("email") String email,
                                @RequestParam("phone") String phone,
                                @RequestParam("address") String address,
                                @RequestParam("imageFile") MultipartFile file,
                                Principal principal,
                                RedirectAttributes redirectAttributes) throws IOException {

        if (principal == null) {
            return "redirect:/login";
        }

        String emailFromSession = principal.getName();
        User user = userService.findByEmail(emailFromSession)
                .orElseThrow(() -> new RuntimeException("User not found"));


        if (!user.getEmail().equals(email)) {
            Optional<User> existingEmailUser = userService.findByEmail(email);
            if (existingEmailUser.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Email already exists!");
                return "redirect:/profile";
            }
        }


        if (!user.getPhone().equals(phone)) {
            Optional<User> existingPhoneUser = userService.findByPhone(phone);
            if (existingPhoneUser.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Phone number already exists!");
                return "redirect:/profile";
            }
        }


        user.setName(name);
        user.setEmail(email);
        user.setPhone(phone);
        user.setAddress(address);


        if (!file.isEmpty()) {
            String uploadDir = "C:/uploads/";
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            File uploadPath = new File(uploadDir);
            if (!uploadPath.exists()) {
                uploadPath.mkdirs();
            }

            file.transferTo(new File(uploadDir + fileName));
            user.setProfileImage(fileName);
        }


        userService.update(user);


        //session.setAttribute("user", user);

        redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");
        return "redirect:/profile";
    }

}
