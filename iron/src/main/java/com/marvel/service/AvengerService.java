package com.marvel.service;

import com.marvel.entity.Avenger;
import com.marvel.entity.Demoorder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by gaoyang on 18/6/18.
 */
@Service
public class AvengerService {

    @Autowired
    private AvengerRepository avengerRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RedisService redisService;

    @Transactional
    public void addTwoHero(){
        Avenger avenger=new Avenger();
        avenger.setName("haoke");
        avenger.setAge(30);
        avengerRepository.save(avenger);

        Avenger avenger2=new Avenger();
        avenger2.setName("heiguafu");
        avenger2.setAge(100/0);
        avengerRepository.save(avenger2);
    }


    public String test(){
        try {
            if(redisService.lockWithTimeout("test",5000,3000)){
                Avenger avenger = avengerRepository.findById(4).get();
                int kc=avenger.getAge();
                System.out.println("当前库存:"+avenger.getAge());
                if(0==avenger.getAge()){
                    return "没库存了";
                }
                avenger.setAge(avenger.getAge()-1);
                avengerRepository.save(avenger);
                Demoorder demoorder=new Demoorder();
                demoorder.setName(Thread.currentThread().getName());
                demoorder.setAge(kc);
                orderRepository.save(demoorder);
                System.out.println("剩余库存:"+avenger.getAge());
                return ""+avenger.getAge();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            redisService.releaseLock("test");
        }

        return "又点问题";
    }
}
