package com.economigos.economigosfiles.services;

import com.economigos.economigosfiles.dtos.*;
import com.economigos.economigosfiles.form.GastoForm;
import com.economigos.economigosfiles.form.RendaForm;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EconomigosService {

    public static String getBaseUrl(){
        String baseUrl = "";

        if (System.getenv("SERVICE_URL") != null){
            baseUrl = System.getenv("SERVICE_URL");
        }else{
            baseUrl = "http://ec2-34-236-53-23.compute-1.amazonaws.com:8080";
        }

        return baseUrl;
    }

    public static QroFeriasDto requestLancamentosByUsuario(Long idUsuario){
        RestTemplate restTemplate = new RestTemplate();
        QroFeriasDto qroFeriasDto = restTemplate.getForObject( getBaseUrl() + "/economigos/usuarios/lancamentos?idUsuario="+idUsuario, QroFeriasDto.class);

        return qroFeriasDto;
    }

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
