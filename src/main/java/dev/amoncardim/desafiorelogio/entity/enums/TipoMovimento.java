package dev.amoncardim.desafiorelogio.entity.enums;

public enum TipoMovimento {
    QUARTZ, AUTOMATICO, MANUAL;

    public static TipoMovimento fromApi(String valor) {
        if (valor == null || valor.isBlank()) return null;
        return switch (valor) {
            case "quartz" -> QUARTZ;
            case "automatic" -> AUTOMATICO;
            case "manual" -> MANUAL;
            default -> throw new IllegalArgumentException("Tipo movimento inválido" + valor);
        };
    }

    public String toApi() {
        return switch (this) {
            case QUARTZ -> "quartz";
            case AUTOMATICO -> "automatic";
            case MANUAL -> "manual";
        };
    }
}
