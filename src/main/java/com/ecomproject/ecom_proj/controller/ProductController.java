package com.ecomproject.ecom_proj.controller;

import com.ecomproject.ecom_proj.model.Product;
import com.ecomproject.ecom_proj.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService service;
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        return new ResponseEntity<>(service.getAllProducts(), HttpStatus.OK);
    }
    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable int id){
        Product product = service.getProductById(id);
        if(product!=null)
            return new ResponseEntity<>(service.getProductById(id),HttpStatus.OK);
        else
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
    @PostMapping("/products")
    public ResponseEntity<?> addProduct(@RequestPart Product product,
                                        @RequestPart MultipartFile imageFile){
        try {
            Product product1=service.addProduct(product, imageFile);
            return new ResponseEntity<>(product1, HttpStatus.CREATED);
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/products/{productId}/image")
    public ResponseEntity<byte[]> getImageById(@PathVariable int productId){
        Product product=service.getProductById(productId);
        byte[] imageFile = product.getImageData();
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(product.getImageType()))
                .body(imageFile);
    }
//    @PutMapping("/products/{id}")
//    public ResponseEntity<String> updateProduct(@PathVariable int id,@RequestPart Product product,
//                                                @RequestPart MultipartFile imageFile){
//        Product product1= null;
//        try {
//            product1 = service.updateProduct(id,product,imageFile);
//        } catch (IOException e) {
//            return new ResponseEntity<>("Failed to update",HttpStatus.BAD_REQUEST);
//        }
//        if(product1!=null)
//            return new ResponseEntity<>("Updated",HttpStatus.OK);
//        else
//            return new ResponseEntity<>("Failed to update",HttpStatus.BAD_REQUEST);
//    }
@PutMapping(value = "/products/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ResponseEntity<String> updateProduct(@PathVariable int id,
                                            @RequestPart("product") Product product,
                                            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {
    try {
        Product updated = service.updateProduct(id, product, imageFile);
        if (updated != null) {
            return new ResponseEntity<>("Updated", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
        }
    } catch (IOException e) {
        return new ResponseEntity<>("Failed to update", HttpStatus.BAD_REQUEST);
    }
}

    @DeleteMapping("/products/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id){
        Product product=service.getProductById(id);
        if(product!=null){
            service.deleteProduct(id);
            return new ResponseEntity<>("Deleted", HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword){
        System.out.println("searching for: " + keyword);
        List<Product> products= service.searchProducts(keyword);
        return new ResponseEntity<>(products,HttpStatus.OK);
    }
}
