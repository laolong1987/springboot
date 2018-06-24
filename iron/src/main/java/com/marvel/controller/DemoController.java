package com.marvel.controller;

import com.marvel.bean.Hero;
import com.marvel.entity.Avenger;
import com.marvel.service.AvengerRepository;
import com.marvel.service.AvengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Created by gaoyang on 18/6/17.
 */
@RestController
public class DemoController {

    @Value("${cupSize}")
    private String cupSize;

    @Value("${content}")
    private String content;


    @Autowired
    private Hero hero;

    @Autowired
    private AvengerRepository avengerRepository;

    @Autowired
    private AvengerService avengerService;



    @RequestMapping(value = "/hello",method = RequestMethod.GET)
    public String say(){
        return hero.getName()+hero.getAge();
    }


    @RequestMapping(value = "/hello/{id}",method = RequestMethod.GET)
    public String say1(@PathVariable Integer id){
        return "id="+id;
    }

    @RequestMapping(value = "/hello2",method = RequestMethod.GET)
    public String say2(@RequestParam(value = "id",required = false,defaultValue = "0") Integer id){
        return "id="+id;
    }

    @GetMapping(value = "/hello3")
    public String say3(@RequestParam Integer id){
        return "id="+id;
    }


    /**
     * 查询列表
     * @return
     */
    @GetMapping(value = "/findAvenger")
    public List<Avenger> findAvenger(){
        return avengerRepository.findAll();
    }

    /**
     * 查询列表
     * @return
     */
    @GetMapping(value = "/findAvenger/age/{age}")
    public List<Avenger> findAvengerByAge(@PathVariable Integer age){
        return avengerRepository.findByAge(age);
    }

    /**
     * 添加对象
     * @param name
     * @param age
     * @return
     */
    @PostMapping(value = "/addAvenger")
    public Avenger addAvenger(@RequestParam("name") String name,@RequestParam("age") Integer age){
        Avenger avenger=new Avenger();
        avenger.setAge(age);
        avenger.setName(name);
        return avengerRepository.save(avenger);
    }


    /**
     * 获取对象
     * @param id
     * @return
     */
    @GetMapping(value = "/avenger/{id}")
    public Avenger getAvenger(@PathVariable("id") Integer id){
        return avengerRepository.findById(id).get();
    }

    /**
     * 修改对象
     * @param id
     * @param age
     * @return
     */
    @PutMapping(value = "/avenger/{id}")
    public Avenger updateAvenger(@PathVariable("id") Integer id,@RequestParam("age") Integer age){

        Avenger avenger=avengerRepository.findById(id).get();
        avenger.setAge(age);
        return avengerRepository.save(avenger);
    }


    /**
     * 删除对象
     * @param id
     */
    @DeleteMapping(value = "/avenger/{id}")
    public void deleteAvenger(@PathVariable("id") Integer id){
         avengerRepository.deleteById(id);
    }


    /**
     * 事务处理
     */
    @PostMapping(value = "/addAvenger2")
    public void  addAvenger2(){
        avengerService.addTwoHero();
    }


    @GetMapping(value = "/redis/add")
    public String testRedis(){
        return  avengerService.test();
    }
}
