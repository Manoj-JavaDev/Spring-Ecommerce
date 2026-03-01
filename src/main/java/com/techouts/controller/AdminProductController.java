package com.techouts.controller;

import com.techouts.model.Product;
import com.techouts.model.User;
import com.techouts.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

@Controller
@RequestMapping("/admin")
public class AdminProductController {

    @Autowired
    private ProductService productService;

    // Show Add Product Page
    @GetMapping("/add-product")
    public String showAddProductPage(Model model) {
        model.addAttribute("product", new Product());
        return "admin-add-product";
    }


    @PostMapping("/save-product")
    public String saveProduct(
            @RequestParam("name") String name,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("price") BigDecimal price,
            @RequestParam("stock") Integer stock,
            @RequestParam("category") String category,
            @RequestParam("imageFile") MultipartFile file)
            throws IOException {

        Product product = new Product();
        product.setName(name);
        product.setTitle(title);
        product.setDescription(description);
        product.setPrice(price);
        product.setStock(stock);
        product.setCategory(category);
        product.setActive(true);

        // Image Upload
        if (!file.isEmpty()) {
            String uploadDir = "C:/uploads/products/";
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            file.transferTo(new File(uploadDir + fileName));

            product.setImageUrl(fileName);
        }

        productService.save(product);

        return "redirect:/admin/products?success";
    }

    // List all products (Admin)
    @GetMapping("/products")
    public String listProducts(Model model,HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null || user.getRole() != User.UserRole.ROLE_ADMIN) {
            return "redirect:/login";
        }
        model.addAttribute("products", productService.findAll());
        return "admin-product";
    }

    // Delete product (Soft delete)
    @GetMapping("/delete-product/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return "redirect:/admin/products";
    }
}