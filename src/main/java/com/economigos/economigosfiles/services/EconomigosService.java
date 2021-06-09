package com.economigos.economigosfiles.services;

import com.economigos.economigosfiles.dtos.CategoriaDto;
import com.economigos.economigosfiles.dtos.ContaDto;
import com.economigos.economigosfiles.dtos.ContabilUltimasAtividades;
import com.economigos.economigosfiles.dtos.UltimasAtividades;
import com.economigos.economigosfiles.form.GastoForm;
import com.economigos.economigosfiles.form.RendaForm;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@FeignClient("http://localhost:8080/economigos/")
public class EconomigosService {

    public static String getBaseUrl(){
        String baseUrl = "";

        if (System.getenv("SERVICE_URL") != null){
            baseUrl = System.getenv("SERVICE_URL");
        }else{
            baseUrl = "http://ec2-34-236-53-23.compute-1.amazonaws.com:8080/";
        }

        return baseUrl;
    }

    @GetMapping("/usuario/lancamentos")
    public static List<ContabilUltimasAtividades> getLancamentosByUsuario(@RequestParam Long idUsuario)

    public static UltimasAtividades requestContaById(Long idUsuario, Long idConta){
        RestTemplate restTemplate = new RestTemplate();
        System.out.println(getBaseUrl());
        UltimasAtividades ultimasAtividades = restTemplate.getForObject( getBaseUrl() + "/economigos/contas/"+idConta+"/ultimas-atividades?idUsuario="+idUsuario, UltimasAtividades.class);

        return ultimasAtividades;
    }

    public static ContaDto requestContaByApelido(String contaApelido){
        RestTemplate restTemplate = new RestTemplate();
        ContaDto contaDto = restTemplate.getForObject( getBaseUrl() + "/economigos/contas/conta?apelido="+contaApelido.trim(), ContaDto.class);

        return contaDto;
    }

    public static CategoriaDto requestCategoriaByNome(String categoriaNome){
        RestTemplate restTemplate = new RestTemplate();
        CategoriaDto categoriaDto = restTemplate.getForObject( getBaseUrl() + "/economigos/categorias/categoria?categoriaNome="+categoriaNome.trim(), CategoriaDto.class);

        return categoriaDto;
    }

    public static Boolean createRenda(RendaForm renda) {
        try {
            new RestTemplate().postForObject(getBaseUrl() + "/economigos/rendas", renda, RendaForm.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Boolean createGasto(GastoForm gasto) {
        try {
            new RestTemplate().postForObject(getBaseUrl() + "/economigos/gastos", gasto, GastoForm.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
