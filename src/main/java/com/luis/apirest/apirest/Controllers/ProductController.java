package com.luis.apirest.apirest.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.mail.MessagingException;

import com.luis.apirest.apirest.Services.*;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luis.apirest.apirest.Entities.Product;
import com.luis.apirest.apirest.Repositories.ProductRepository;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin(origins = "http://127.0.0.1:5500")

@RestController
@RequestMapping("/Messages")
public class ProductController {

    
    @Autowired
    private emailService emailService;


    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public List<Product> findAllProducts(){
        return productRepository.findAll();
    }

    @GetMapping("/{id}")
    public Product findById(@PathVariable Long id){
        return productRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("No se encontro el producto con el ID: " + id));
    }
    
    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody Product product) {
        try {
            // Construir los datos del correo
            String subject = "Nuevo mensaje de: " + product.getName();
            String emailContent = "<strong>Nombre:</strong> " + product.getName() + "<br>" +
                                  "<strong>Teléfono:</strong> " + product.getNumber() + "<br>" +
                                  "<strong>Email:</strong> " + product.getEmail() + "<br>" +
                                  "<strong>Mensaje:</strong> " + product.getMessage();
    
            // Enviar los datos por correo
            emailService.sendEmail("luis.j.fcfm@gmail.com", subject, emailContent);
    
            // Guardar en la base de datos
            Product savedProduct = productRepository.save(product);
            return ResponseEntity.ok(savedProduct);
    
        } catch (MessagingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al enviar el correo: " + e.getMessage());
        }
    }
    

    
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product productDetails){
        Product product = productRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("No se encontro el producto con el ID: " + id));

        product.setName(productDetails.getName());
        product.setNumber(productDetails.getNumber());
        product.setEmail(productDetails.getEmail());
        product.setMessage(productDetails.getMessage());

        return productRepository.save(product);
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable Long id){
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("No se encontro el producto con el ID: " + id));

        productRepository.delete(product);
        return "El producto con el ID: " + id + " fue eliminado correctamente";
    }
}
