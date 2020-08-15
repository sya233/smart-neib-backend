package com.upai.smartneib.alipay;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AliOrderRepository extends CrudRepository<Aliorder, Long> {

    List<Aliorder> findOrderByUser(String user);

}
