package com.marvel.service;

import com.marvel.entity.Demoorder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * Created by gaoyang on 18/6/18.
 */
@Component
public interface OrderRepository extends JpaRepository<Demoorder,Integer>{



}
