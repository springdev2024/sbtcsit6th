package com.example.sbtcsit6th.admin;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.sbtcsit6th.AuthService;
import com.example.sbtcsit6th.ValidationError;
import com.example.sbtcsit6th.product.Product;
import com.example.sbtcsit6th.product.ProductRepository;
import com.example.sbtcsit6th.user.User;
import com.example.sbtcsit6th.user.UserType;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AdminController {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private AuthService authService;

	@GetMapping("/admin")
	public String getDashboard(HttpServletRequest request) {

		User user = authService.getUser(request);

		// Authentication
		if (user == null) {
			System.out.println("No logged in user found");
			return "redirect:/login";
		}

		// Authorization
		if (user.getType() != UserType.ADMIN) {
			System.out.println("ADMIN type expected");
			return "redirect:/login";
		}

		return "admin-dashboard.html";
	}

	@GetMapping("/admin/product")
	public String getProductPage(Model model, HttpServletRequest request) {
		User user = authService.getUser(request);
		
		// Authentication & Authorization respectively
		if (user == null || user.getType() != UserType.ADMIN) {
			System.out.println("No logged in user found");
			return "redirect:/login";
		}

		model.addAttribute("products", productRepository.findAll());
		return "view-product.html";
	}

	@GetMapping("/admin/product/add")
	public String getAddProductPage(HttpServletRequest request, Model model) {

		User user = authService.getUser(request);

		// Authentication & Authorization respectively
		if (user == null || user.getType() != UserType.ADMIN) {
			System.out.println("No logged in user found");
			return "redirect:/login";
		}

		model.addAttribute("product", new Product());
		model.addAttribute("error", new ValidationError());
		return "add-product.html";
	}

	@PostMapping("/admin/product/add")
	public String addNewProduct(Product product, Model model, HttpServletRequest request) {

		User user = authService.getUser(request);

		// Authentication & Authorization respectively
		if (user == null || user.getType() != UserType.ADMIN) {
			System.out.println("No logged in user found");
			return "redirect:/login";
		}

		// TODO: laptop bag -> Laptop Bag

		product.setName(product.getName().strip());
		product.setUnit(product.getUnit().strip());
		product.setDescription(product.getDescription().strip());

		ValidationError error = null;

		if (product.getName().isEmpty()) {
			error = new ValidationError("Product name is required");
		}

		if (product.getUnit().isEmpty()) {
			error = new ValidationError("Unit is required");
		}

		if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
			error = new ValidationError("Price must be positive");
		}

		if (error != null) {
			model.addAttribute("error", error);
			return "add-product.html";
		}

		// save the product
		productRepository.save(product);

		return "redirect:/";
	}

}
