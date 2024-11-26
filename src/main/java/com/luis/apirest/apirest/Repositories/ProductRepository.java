package com.luis.apirest.apirest.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luis.apirest.apirest.Entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{

}
