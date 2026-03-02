package com.techouts.controller;

import com.techouts.model.Product;
import com.techouts.model.User;
import com.techouts.service.ProductService;
import com.techouts.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;
    @GetMapping("/products")
    public String listProducts(@RequestParam(value = "category", required = false) String category,
                               @RequestParam(value = "search", required = false) String search,
                               Principal principal,
                               HttpSession session,
                               Model model) {

        List<Product> products;

        if (search != null && !search.trim().isEmpty()) {
            products = productService.search(search.trim());
        } else if (category != null && !category.trim().isEmpty()) {
            products = productService.findByCategory(category);
        } else {
            products = productService.findAll();
        }

        model.addAttribute("products", products);

        if (principal != null) {
            userService.findByEmail(principal.getName()).ifPresent(user -> {
                session.setAttribute("user", user);
                System.out.println("Logged-in user: " + user.getName() + " | " + user.getEmail());
            });
        }

        return "index";
    }

    @GetMapping("/productDetails/{id}")
    public String productDetails(@PathVariable("id") Long id, Model model) {
        Optional<Product> productOpt = productService.findById(id);

        if (productOpt.isEmpty()) {
            return "redirect:/products"; // or show a 404 page
        }

        model.addAttribute("product", productOpt.get());
        return "pdp"; // returns pdp.jsp
    }


}
