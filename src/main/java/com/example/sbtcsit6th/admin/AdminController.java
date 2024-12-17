package com.example.sbtcsit6th.admin;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.sbtcsit6th.ValidationError;
import com.example.sbtcsit6th.product.Product;
import com.example.sbtcsit6th.product.ProductRepository;

@Controller
public class AdminController {

	@Autowired
	private ProductRepository productRepository;
	
	@GetMapping("/admin")
	public String getDashboard() {
		return "admin-dashboard.html";
	}

	@GetMapping("/admin/product")
	public String getProductPage(Model model) {
		model.addAttribute("products", productRepository.findAll());
		return "view-product.html";
	}

	@GetMapping("/admin/product/add")
	public String getAddProductPage(Model model) {
		model.addAttribute("product", new Product());
		model.addAttribute("error", new ValidationError());
		return "add-product.html";
	}

	@PostMapping("/admin/product/add")
	public String addNewProduct(Product product, Model model) {

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
