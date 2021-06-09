package com.economigos.economigosfiles.services;

import com.economigos.economigosfiles.dtos.ContabilUltimasAtividades;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "economigos", url = "http://localhost:8080/economigos/")
public interface EconomigosClient {

    @GetMapping("/usuario/lancamentos")
    List<ContabilUltimasAtividades> get(@RequestParam Long idUsuario);

}
