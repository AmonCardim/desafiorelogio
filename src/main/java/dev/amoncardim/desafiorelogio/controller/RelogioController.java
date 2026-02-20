package dev.amoncardim.desafiorelogio.controller;

import dev.amoncardim.desafiorelogio.dto.AtualizarRelogioRequest;
import dev.amoncardim.desafiorelogio.dto.CriarRelogioRequest;
import dev.amoncardim.desafiorelogio.dto.PaginaRelogioDto;
import dev.amoncardim.desafiorelogio.dto.RelogioDto;
import dev.amoncardim.desafiorelogio.service.RelogioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/relogios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RelogioController {

    private final RelogioService servico;

    @GetMapping
    public PaginaRelogioDto listar(
            @RequestParam(defaultValue = "1") int pagina,
            @RequestParam(defaultValue = "12") int porPagina,
            @RequestParam(required = false) String busca,
            @RequestParam(required = false) String  marca,
            @RequestParam(required = false) String tipoMovimento,
            @RequestParam(required = false) String materialCaixa,
            @RequestParam(required = false) String tipoVidro,
            @RequestParam(required = false) Integer resistenciaMin,
            @RequestParam(required = false) Integer resistenciaMax,
            @RequestParam(required = false) Long precoMin,
            @RequestParam(required = false) Long precoMax,
            @RequestParam(required = false) Integer diametroMin,
            @RequestParam(required = false) Integer diametroMax,
            @RequestParam(required = false) String ordenar
    ) {
        return servico.listar(pagina,
                porPagina,
                busca,
                marca,
                tipoMovimento,
                materialCaixa,
                tipoVidro,
                resistenciaMin,
                resistenciaMax,
                precoMin,
                precoMax,
                diametroMin,
                diametroMax,
                ordenar);
    }

    @GetMapping("/{id}")
    public RelogioDto buscarPorId(@PathVariable UUID id) {
        return servico.buscarPorId(id);
    }

    public RelogioDto criar(@Valid @RequestBody CriarRelogioRequest req) {
        return servico.criar(req);
    }

    @PutMapping("/{id}")
    public RelogioDto atualizar(@PathVariable UUID id, @Valid @RequestBody AtualizarRelogioRequest req) {
        return servico.atualizar(id, req);
    }

    @DeleteMapping("/{id}")
    public void remover(@PathVariable UUID id) {
        servico.remover(id);
    }
}