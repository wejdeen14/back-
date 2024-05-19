package com.example.restaurant_universitaire.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.restaurant_universitaire.Model.bondecommande;

@Repository
public interface bondcommandeRepository extends JpaRepository<bondecommande, Long> {

    bondecommande findByIdCmd(Long id);

}
