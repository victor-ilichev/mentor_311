package com.example.crud.service;

import com.example.crud.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarProvider extends JpaRepository<Car, Long> {

}
