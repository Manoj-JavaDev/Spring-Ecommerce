package com.techouts.controller;

import com.techouts.model.Cart;
import com.techouts.model.CartProducts;
import com.techouts.model.Product;
import com.techouts.model.User;
import com.techouts.service.CartService;
import com.techouts.service.ProductService;
import com.techouts.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @PostMapping("/add/{productId}")
    public String addToCart(@PathVariable("productId") Long productId, HttpSession session) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        Optional<Product> findProduct= productService.findById(productId);
        Product product = findProduct.orElse(null);
        cartService.addToCart(user, product);
        return "redirect:/cart/view";
    }

    @GetMapping("/view")
    public String viewCart(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        //System.out.println(user.getName());
        if (user == null) return "redirect:/login";

        Cart cart = cartService.getCart(user);
        List<CartProducts> cartProducts = cart != null ? cartService.getCartProducts(cart) : null;

        model.addAttribute("cartProducts", cartProducts);
        return "cart";
    }

    @PostMapping("/updateQuantity")
    public String updateQuantity(@RequestParam("cartProductId") Long cpId,
                                 @RequestParam("quantity") int quantity) {
        if (quantity <= 0) {
            cartService.removeFromCart(cpId);
        } else {
            cartService.updateQuantity(cpId, quantity);
        }
        return "redirect:/cart/view";
    }

    @PostMapping("/remove")
    public String removeFromCart(@RequestParam("cartProductId") Long cpId) {
        cartService.removeFromCart(cpId);
        return "redirect:/cart/view";
    }


}
