package dev.amoncardim.desafiorelogio.config;

import dev.amoncardim.desafiorelogio.entity.Relogio;
import dev.amoncardim.desafiorelogio.entity.enums.MaterialCaixa;
import dev.amoncardim.desafiorelogio.entity.enums.TipoMovimento;
import dev.amoncardim.desafiorelogio.repository.RelogioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class CarregadorDadosInicial {

    private final RelogioRepository relogioRepository;

    @Bean
    CommandLineRunner seedRelogios() {
        return args -> {
            if (relogioRepository.count() > 0) return;

            Instant agora = Instant.now();

            List<Relogio> relogios = List.of(
                    Relogio.builder()
                            .id(UUID.randomUUID())
                            .marca("Casio")
                            .modelo("F-150")
                            .referencia("1234")
                            .tipoMovimento(TipoMovimento.QUARTZ)
                            .materialCaixa(MaterialCaixa.RESINA)
                            .resistenciaAguaM(30)
                            .diametroMm(35)
                            .lugToLugMm(38)
                            .espessuraMm(9)
                            .larguraLugMm(18)
                            .precoEmCentavos(12990)
                            .urlImagem("123")
                            .criadoEm(agora.minusSeconds(50000))
                            .build(),
                    Relogio.builder()
                            .id(UUID.randomUUID())
                            .marca("Omega")
                            .modelo("HTS-980")
                            .referencia("332")
                            .tipoMovimento(TipoMovimento.AUTOMATICO)
                            .materialCaixa(MaterialCaixa.TITANIO)
                            .resistenciaAguaM(200)
                            .diametroMm(40)
                            .lugToLugMm(40)
                            .espessuraMm(12)
                            .larguraLugMm(20)
                            .precoEmCentavos(99999)
                            .urlImagem("98967")
                            .criadoEm(agora.minusSeconds(50000))
                            .build()
            );

            relogioRepository.saveAll(relogios);
        };
    }
}
