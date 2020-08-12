package com.upai.smartneib.notification;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NotificationRepository extends CrudRepository<Notification, Long> {

    public List<Notification> findAll();

}
