package com.example.sbtcsit6th.admin;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.sbtcsit6th.AuthService;
import com.example.sbtcsit6th.StorageService;
import com.example.sbtcsit6th.ValidationError;
import com.example.sbtcsit6th.product.CreateProductForm;
import com.example.sbtcsit6th.product.Product;
import com.example.sbtcsit6th.product.ProductCategory;
import com.example.sbtcsit6th.product.ProductCategoryRepository;
import com.example.sbtcsit6th.product.ProductRepository;
import com.example.sbtcsit6th.user.User;
import com.example.sbtcsit6th.user.UserType;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AdminController {

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ProductCategoryRepository productCategoryRepository;

	@Autowired
	private AuthService authService;
	
	@Autowired
	private StorageService storageService;

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
		model.addAttribute("productCategories", productCategoryRepository.findAll());
		return "add-product.html";
	}

	@PostMapping("/admin/product/add")
	public String addNewProduct(@ModelAttribute CreateProductForm form, Model model, HttpServletRequest request) {
//	public String addNewProduct(Product product, Model model, HttpServletRequest request) {

		User user = authService.getUser(request);

		// Authentication & Authorization respectively
		if (user == null || user.getType() != UserType.ADMIN) {
			System.out.println("No logged in user found");
			return "redirect:/login";
		}

		form.setName(form.getName().strip());
		form.setUnit(form.getUnit().strip());
		form.setDescription(form.getDescription().strip());

		ValidationError error = null;

		if (form.getName().isEmpty()) {
			error = new ValidationError("Product name is required");
		}

		if (form.getUnit().isEmpty()) {
			error = new ValidationError("Unit is required");
		}

		if (form.getPrice() == null || form.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
			error = new ValidationError("Price must be positive");
		}

		if (error != null) {
			model.addAttribute("error", error);
			return "add-product.html";
		}
		
		Product product = new Product();
		product.setCategory(form.getCategory());
		product.setDescription(form.getDescription());
		product.setName(form.getName());
		product.setPrice(form.getPrice());
		product.setUnit(form.getUnit());
		
		String fileName = storageService.store(form.getImage());;
		product.setImage(fileName);
		
		// save the product
		productRepository.save(product);

		return "redirect:/admin/product";
	}
	
	@GetMapping("/admin/productCategory")
	public String getProductCategoryPage(Model model, HttpServletRequest request) {
		User user = authService.getUser(request);
		
		// Authentication & Authorization respectively
		if (user == null || user.getType() != UserType.ADMIN) {
			System.out.println("No logged in user found");
			return "redirect:/login";
		}

		model.addAttribute("productCategories", productCategoryRepository.findAll());
		return "view-productCategory.html";
	}
	
	@GetMapping("/admin/productCategory/add")
	public String getAddProductCategoryPage(HttpServletRequest request, Model model) {

		User user = authService.getUser(request);

		// Authentication & Authorization respectively
		if (user == null || user.getType() != UserType.ADMIN) {
			System.out.println("No logged in user found");
			return "redirect:/login";
		}

		model.addAttribute("productCategory", new ProductCategory());
		model.addAttribute("error", new ValidationError());
		return "add-productCategory.html";
	}
	
	@PostMapping("/admin/productCategory/add")
	public String addNewProductCategory(ProductCategory productCategory, Model model, HttpServletRequest request) {

		User user = authService.getUser(request);

		// Authentication & Authorization respectively
		if (user == null || user.getType() != UserType.ADMIN) {
			System.out.println("No logged in user found");
			return "redirect:/login";
		}

		productCategory.setName(productCategory.getName().strip());
		productCategory.setDescription(productCategory.getDescription().strip());

		ValidationError error = null;

		if (productCategory.getName().isEmpty()) {
			error = new ValidationError("Product Category name is required");
		}

		if (error != null) {
			model.addAttribute("error", error);
			return "add-productCategory.html";
		}

		// save the product
		productCategoryRepository.save(productCategory);

		return "redirect:/admin/productCategory";
	}

}
