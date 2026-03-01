package com.techouts.model;

import com.techouts.model.CartProducts;
import com.techouts.model.User;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(mappedBy = "cart")
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartProducts> cartProducts = new ArrayList<>();

    // ========================
    // Helper Methods
    // ========================

    public void addProduct(Product product) {

        // Check if product already exists
        for (CartProducts cp : cartProducts) {
            if (cp.getProduct().getId().equals(product.getId())) {
                cp.setQuantity(cp.getQuantity() + 1);
                return;
            }
        }

        // If not exists, create new
        CartProducts cp = new CartProducts();
        cp.setCart(this);
        cp.setProduct(product);
        cp.setQuantity(1);

        cartProducts.add(cp);
    }

    public void removeProduct(Product product) {
        cartProducts.removeIf(cp ->
                cp.getProduct().getId().equals(product.getId()));
    }

    public BigDecimal getTotalAmount() {
        BigDecimal total = BigDecimal.ZERO;
        for (CartProducts cp : cartProducts) {
            assert false;
            total.add(cp.getProduct().getPrice().multiply(BigDecimal.valueOf(cp.getQuantity())));
        }
        return total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<CartProducts> getCartProducts() {
        return cartProducts;
    }


    public int getTotalItems() {
        int count = 0;
        for (CartProducts cp : cartProducts) {
            count += cp.getQuantity();
        }
        return count;
    }

    // getters and setters
}