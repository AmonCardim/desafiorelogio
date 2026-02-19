package dev.amoncardim.desafiorelogio.service;

import dev.amoncardim.desafiorelogio.entity.Relogio;
import org.springframework.data.jpa.domain.Specification;

public class RelogioSpecs {

    private RelogioSpecs() {}

    public static Specification<Relogio> tudo(){
        return (root, query, cb) -> cb.conjunction();
    }

    public static boolean blank(String str) {
        return  str == null || str.isBlank();
    }

    /**
     * WHERE
     * LOWER(marca) LIKE '%termo%'
     * OR
     * LOWER(modelo) LIKE '%termo%'
     * OR
     * LOWER(referencia) LIKE '%termo%'
     */
    public static Specification<Relogio> busca(String termo) {
        if (blank(termo)) return tudo();
        String like = "%" + termo.toLowerCase() + "%";
        return (root, q, cb) -> cb.or(
                cb.like(cb.lower(root.get("marca")), like),
                cb.like(cb.lower(root.get("modelo")), like),
                cb.like(cb.lower(root.get("referencia")), like)
        );
    }
}
