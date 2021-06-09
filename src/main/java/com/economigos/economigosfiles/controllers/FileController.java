package com.economigos.economigosfiles.controllers;

import com.economigos.economigosfiles.dtos.ContabilUltimasAtividades;
import com.economigos.economigosfiles.dtos.QroFeriasDto;
import com.economigos.economigosfiles.dtos.UltimasAtividades;
import com.economigos.economigosfiles.models.Imagem;
import com.economigos.economigosfiles.repositories.AnexoRepository;
import com.economigos.economigosfiles.repositories.ImagemRepository;
import com.economigos.economigosfiles.services.EconomigosService;
import com.economigos.economigosfiles.utils.fileio.GravaArquivo;
import com.economigos.economigosfiles.utils.fileio.LeArquivo;
import com.economigos.economigosfiles.utils.structures.PilhaObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.*;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/economigos/files")
public class FileController {

    @Autowired
    EconomigosService economigosService;
    @Autowired
    AnexoRepository anexoRepository;
    @Autowired
    ImagemRepository imagemRepository;

    @GetMapping("/export/{idConta}")
    @Transactional
    public ResponseEntity<Resource> getExtrato(@RequestParam Long idUsuario,
                                               @PathVariable Long idConta,
                                               @RequestParam Boolean csvFile) throws FileNotFoundException {
        UltimasAtividades ultimasAtividades = EconomigosService.requestContaById(idUsuario, idConta);

        List<ContabilUltimasAtividades> atividadesList = ultimasAtividades.getContabilUltimasAtividadesDtos();

        int tamanhoRetorno = atividadesList.size();

        Collections.sort(atividadesList);

        PilhaObj<ContabilUltimasAtividades> ultimasAtividadesPilhaObj = new PilhaObj<>(tamanhoRetorno);

        String fileName = "extrato";

        if (tamanhoRetorno > 0) {
            for (ContabilUltimasAtividades contabilUltimasAtividades : atividadesList) {
                ultimasAtividadesPilhaObj.push(contabilUltimasAtividades);
            }
            try {
                GravaArquivo.gravaPilha(ultimasAtividadesPilhaObj, fileName, csvFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

            InputStreamResource resource = new InputStreamResource(new FileInputStream(GravaArquivo.getFile()));

            if (GravaArquivo.getFile().getName().endsWith(".csv")){
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName + ".csv")
                        .contentType(MediaType.parseMediaType("application/csv"))
                        .body(resource);
            }else{
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName + ".txt")
                        .contentType(MediaType.parseMediaType("application/txt"))
                        .body(resource);
            }
        }

        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/export")
    @Transactional
    public ResponseEntity<Resource> getExtrato(@RequestParam Long idUsuario,
                                               @RequestParam Boolean csvFile) throws FileNotFoundException {

        QroFeriasDto a = EconomigosService.requestLancamentosByUsuario(idUsuario);

        List<ContabilUltimasAtividades> atividadesList = a.getContabilUltimasAtividadesDtos();

        int tamanhoRetorno = atividadesList.size();

        PilhaObj<ContabilUltimasAtividades> ultimasAtividadesPilhaObj = new PilhaObj<>(tamanhoRetorno);

        String fileName = "extrato";

        if (tamanhoRetorno > 0) {
            for (ContabilUltimasAtividades contabilUltimasAtividades : atividadesList) {
                ultimasAtividadesPilhaObj.push(contabilUltimasAtividades);
            }
            try {
                GravaArquivo.gravaPilha(ultimasAtividadesPilhaObj, fileName, csvFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

            InputStreamResource resource = new InputStreamResource(new FileInputStream(GravaArquivo.getFile()));

            if (GravaArquivo.getFile().getName().endsWith(".csv")){
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName + ".csv")
                        .contentType(MediaType.parseMediaType("application/csv"))
                        .body(resource);
            }else{
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName + ".txt")
                        .contentType(MediaType.parseMediaType("application/txt"))
                        .body(resource);
            }
        }

        return ResponseEntity.badRequest().build();
    }


    @PostMapping("/import")
    @Transactional
    public ResponseEntity<String> importart(@RequestParam MultipartFile arquivo,
                                            @RequestParam Boolean imagem) throws IOException {
        if(!arquivo.isEmpty()){

            if (!imagem) {
                File file = new File(arquivo.getOriginalFilename());

                try (OutputStream os = new FileOutputStream(file)) {
                    os.write(arquivo.getBytes());
                }

                anexoRepository.save(LeArquivo.leArquivo(new FileReader(file), arquivo.getOriginalFilename()));

                return ResponseEntity.ok().body("Arquivo guardado.");
            }else{
                imagemRepository.save(new Imagem(arquivo.getOriginalFilename(), arquivo.getBytes()));

                return ResponseEntity.ok().body("Imagem guardada.");
            }

        }else{
            return ResponseEntity.badRequest().body("Arquivo vazio.");
        }
    }

    @GetMapping(
            value = "/images/{nomeImagem}",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public @ResponseBody byte[] getImageWithMediaType(@PathVariable String nomeImagem) throws IOException {

        return imagemRepository.findByNome(nomeImagem).get().getConteudo();
    }

}
