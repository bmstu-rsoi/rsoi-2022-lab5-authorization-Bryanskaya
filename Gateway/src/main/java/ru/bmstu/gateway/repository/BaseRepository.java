package ru.bmstu.gateway.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import ru.bmstu.gateway.config.AppParams;

import javax.annotation.Resource;

@Repository
public class BaseRepository {
    @Resource
    public WebClient webClient;

    @Autowired
    public AppParams appParams;
}
