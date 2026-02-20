package dev.amoncardim.desafiorelogio.service;

import dev.amoncardim.desafiorelogio.dto.CriarRelogioRequest;
import dev.amoncardim.desafiorelogio.dto.PaginaRelogioDto;
import dev.amoncardim.desafiorelogio.dto.RelogioDto;
import dev.amoncardim.desafiorelogio.entity.Relogio;
import dev.amoncardim.desafiorelogio.entity.enums.MaterialCaixa;
import dev.amoncardim.desafiorelogio.entity.enums.TipoMovimento;
import dev.amoncardim.desafiorelogio.entity.enums.TipoVidro;
import dev.amoncardim.desafiorelogio.mapper.RelogioMapper;
import dev.amoncardim.desafiorelogio.repository.RelogioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

import static dev.amoncardim.desafiorelogio.service.RelogioSpecs.*;

@Service
@RequiredArgsConstructor
public class RelogioService {

    private final RelogioRepository relogioRepository;
    private final RelogioMapper mapeador;

    public PaginaRelogioDto listar(
           int pagina,
           int porPagina,
           String busca,
           String marca,
           String tipoMovimento,
           String materialCaixa,
           String tipoVidro,
           Integer resistenciaMin,
           Integer resistenciaMax,
           Long precoMin,
           Long precoMax,
           Integer diametroMin,
           Integer diamentroMax,
           String ordenar
    ) {
        int paginaSegura = Math.max(1, pagina);
        int porPaginaSegura = Math.min(60, Math.max(1, porPagina));

        TipoMovimento movimento = TipoMovimento.fromApi(tipoMovimento);
        MaterialCaixa material = MaterialCaixa.fromApi(materialCaixa);
        TipoVidro vidro = TipoVidro.fromApi(tipoVidro);

        OrdenacaoRelogios ordenacao = OrdenacaoRelogios.fromApi(ordenar);

        Sort sort = switch (ordenacao) {
            case MAIS_RECENTES -> Sort.by(Sort.Direction.DESC, "criadoEm");
            case PRECO_CRESC ->  Sort.by(Sort.Direction.ASC, "precoEmCentavos");
            case PRECO_DESC ->  Sort.by(Sort.Direction.DESC, "precoEmCentavos");
            case DIAMETRO_CRESC ->   Sort.by(Sort.Direction.ASC, "diametroMm");
            case RESISTENCIA_DESC ->   Sort.by(Sort.Direction.DESC, "resistenciaAguaM");
        };

        Pageable pageable = PageRequest.of(paginaSegura - 1, paginaSegura, sort);

        Specification<Relogio> spec = Specification.where(busca(busca))
                .and(marcaIgual(marca))
                .and(tipoMovimentoIgual(movimento))
                .and(materialCaixaIgual(material))
                .and(tipoVidroIgual(vidro))
                .and(resistenciaAguaEntre(resistenciaMin, resistenciaMax))
                .and(precoEntre(precoMin, precoMax))
                .and(diametroEntre(diametroMin, diamentroMax));

        Page<Relogio> resultado = relogioRepository.findAll(spec, pageable);

        return new PaginaRelogioDto(
                resultado.getContent().stream().map(mapeador::toDto).toList(),
                resultado.getTotalElements()
        );
    }

    public RelogioDto buscarPorId(UUID id) {
        Relogio r =  relogioRepository.findById(id).orElseThrow(() -> new NaoEncontradoException("Relógio não encontrado: " + id));
        return mapeador.toDto(r);
    }

    public RelogioDto criar(CriarRelogioRequest req) {
        Relogio r = Relogio.builder()
                .id(UUID.randomUUID())
                .marca(req.marca())
                .modelo(req.modelo())
                .referencia(req.referencia())
                .tipoMovimento(TipoMovimento.fromApi(req.tipoMovimento()))
                .materialCaixa(MaterialCaixa.fromApi(req.materialCaixa()))
                .tipoVidro(TipoVidro.fromApi(req.tipoVidro()))
                .resistenciaAguaM(req.resistenciaAguaM())
                .diametroMm(req.diametroMm())
                .lugToLugMm(req.lugToLugMm())
                .espessuraMm(req.espessuraMm())
                .larguraLugMm(req.larguraLugMm())
                .precoEmCentavos(req.precoEmCentavos())
                .urlImagem(req.urlImagem())
                .criadoEm(Instant.now())
                .build();
        return mapeador.toDto(relogioRepository.save(r));
    }

    public RelogioDto atualizar (UUID id, CriarRelogioRequest req) {
        Relogio r = relogioRepository.findById(id)
                .orElseThrow(() -> new NaoEncontradoException("Relógio não encontrado: " + id));
        r.setMarca(req.marca());
        r.setModelo(req.modelo());
        r.setReferencia(req.referencia());
        r.setTipoMovimento(TipoMovimento.fromApi(req.tipoMovimento()));
        r.setMaterialCaixa(MaterialCaixa.fromApi(req.materialCaixa()));
        r.setTipoVidro(TipoVidro.fromApi(req.tipoVidro()));
        r.setResistenciaAguaM(req.resistenciaAguaM());
        r.setDiametroMm(req.diametroMm());
        r.setLugToLugMm(req.lugToLugMm());
        r.setEspessuraMm(req.espessuraMm());
        r.setLarguraLugMm(req.larguraLugMm());
        r.setPrecoEmCentavos(req.precoEmCentavos());
        r.setUrlImagem(req.urlImagem());
        return mapeador.toDto(relogioRepository.save(r));
    }

    public void remover(UUID id,) {
        if (!relogioRepository.existsById(id)) {
            throw new NaoEncontradoException("Relógio não encontrado: " + id);
        }
        relogioRepository.deleteById(id);
    }
}
