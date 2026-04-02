package com.example.warehouse.service;

import com.example.warehouse.model.Product;
import com.example.warehouse.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProductService {

    /*
   Implement the business logic for the ProductService  operations in this class
   Make sure to add required annotations
    */

    @Autowired
    private ProductRepository productRepository;


    //to post all the Product details
    //created->201
    //badRequest->400
    public Object postProduct(Product product) {
       if(productRepository.existsById(product.getId())){
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
       }
           productRepository.save(product);
           return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }


    //to get all the Product details
    //ok->200
    //badRequest->400
    public Object getProduct() {
            List<Product>products = productRepository.findAll();
            if(!products.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(productRepository.findAll());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not found");

    }

    //to get the product with the value(pathVariable)
    //ok()->200
    //badRequest()->400
    public ResponseEntity<Object> getSimilarVendor(String value) {
        List<Product>vendor = productRepository.findByVendor(value);
        if(vendor.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(vendor) ;
    }


    //to update the Product with id as pathVariable and Product as object in RequestBody
    //ok->200
    //badRequest->400
    public ResponseEntity<Object> updateProduct(int id, Product product) {
       if(productRepository.existsById(id)){
           Optional<Product> prod = productRepository.findById(id);
           if(prod.isPresent()) {
               Product existingProd = prod.get();
               existingProd.setPrice(product.getPrice());
               existingProd.setStock(product.getStock());

               productRepository.save(existingProd);
               return ResponseEntity.status(HttpStatus.OK).body(existingProd);
           }
       }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request");
    }


    // to delete the product by using id as PathVariable
    //ok->200
    //badRequest->400
    public ResponseEntity<Object> deleteProductById(int id) {
        if(productRepository.existsById(id)){
            productRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Deleted");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad data");

    }



}
