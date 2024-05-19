package com.example.restaurant_universitaire.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.restaurant_universitaire.Model.ProduitStock;

@Repository
public interface produitstockRepository extends JpaRepository<ProduitStock, Long> {

    ProduitStock findByNomProd(String nomProd);

    boolean existsByNomProd(String nomProd);

}
