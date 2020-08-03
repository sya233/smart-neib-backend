package com.upai.smartneib.update;

import com.upai.smartneib.update.Apk;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UpdateRepository extends CrudRepository<Apk, Long> {

    public List<Apk> findAll();

}
