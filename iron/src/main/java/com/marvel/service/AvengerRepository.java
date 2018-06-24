package com.marvel.service;

import com.marvel.entity.Avenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by gaoyang on 18/6/18.
 */
@Component
public interface AvengerRepository extends JpaRepository<Avenger,Integer>{

    /**
     * 通过年龄查询
     */
    public List<Avenger> findByAge(Integer age);

}
