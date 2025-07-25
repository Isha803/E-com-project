package com.ecomproject.ecom_proj.service;
import com.ecomproject.ecom_proj.model.Product;
import com.ecomproject.ecom_proj.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository repo;
    public List<Product> getAllProducts() {
        return repo.findAll();
    }
    public Product getProductById(int id){
        return repo.findById((long) id).orElse(null);
    }
    public  Product addProduct(Product product, MultipartFile imageFile)throws IOException {
        product.setImageName(imageFile.getOriginalFilename());
        product.setImageType(imageFile.getContentType());
        product.setImageData(imageFile.getBytes());
        return repo.save(product);
    }
//    public Product updateProduct(int id, Product product,MultipartFile imageFile) throws IOException{
//        product.setImageData(imageFile.getBytes());
//        product.setImageName(imageFile.getOriginalFilename());
//        product.setImageType(imageFile.getContentType());
//        return repo.save(product);
//    }
public Product updateProduct(int id, Product product, MultipartFile imageFile) throws IOException {
    // Step 1: Fetch the existing product
    Product existingProduct = repo.findById((long)id).orElse(null);

    if (existingProduct == null) {
        return null; // Or throw exception if preferred
    }

    // Step 2: Update fields from incoming product
    existingProduct.setName(product.getName());
    existingProduct.setDescription(product.getDescription());
    existingProduct.setBrand(product.getBrand());
    existingProduct.setPrice(product.getPrice());
    existingProduct.setCategory(product.getCategory());
    existingProduct.setRegistered_date(product.getRegistered_date());
    existingProduct.setAvailable(product.isAvailable());
    existingProduct.setQuantity(product.getQuantity());

    // Step 3: Handle image update if image is present
    if (imageFile != null && !imageFile.isEmpty()) {
        existingProduct.setImageData(imageFile.getBytes());
        existingProduct.setImageName(imageFile.getOriginalFilename());
        existingProduct.setImageType(imageFile.getContentType());
    }

    // Step 4: Save and return the updated product
    return repo.save(existingProduct);
}

    public void deleteProduct(int id) {
        repo.deleteById((long) id);
     }

    public List<Product> searchProducts(String keyword) {
        return repo.searchProducts(keyword);
    }
}
