package pl.pas.rest;

import pl.pas.rest.model.*;
import pl.pas.rest.repository.TableRepository;
import pl.pas.rest.repository.UserRepository;

import javax.inject.Inject;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.BeanParam;
import javax.ws.rs.core.Application;

@ApplicationPath("/api")
public class RestaurantApplication extends Application {
    @Inject
    private UserRepository userRepository;
    @Inject
    private TableRepository tableRepository;

    @BeanParam
    public void init() {
        userRepository.add(new Administrator(true,"admin1", "pas", "ADMIN", "Adrian", "Wojtasik"));
        userRepository.add(new ResourceManager(true,"manager1", "pas", "MANAGER", "Jakub", "Bugara"));
        userRepository.add(new Client(true, "client1", "pas", "CLIENT", "Marcin", "Muszyński"));

        tableRepository.add(new Table(1,"Stolik dla Java Developerów."));
        tableRepository.add(new Table(2, "Stolik dla zakochanych."));
        tableRepository.add(new Table(3, "Stolik dla rodziny."));
    }

}