package br.edu.fiap.soa.estacionamento.service;

import br.edu.fiap.soa.estacionamento.domain.PrecoTipo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class PrecoService {
    @Value("${parking.hourly-rate:8.00}") private BigDecimal hourlyRate;
    @Value("${parking.daily-rate:60.00}") private BigDecimal dailyRate;
    @Value("${parking.price-policy:HORA}") private String pricePolicy;

    public BigDecimal calcular(LocalDateTime entrada, LocalDateTime saida){
        long minutes = Duration.between(entrada, saida).toMinutes();
        if (minutes <= 0) minutes = 1;
        PrecoTipo tipo = PrecoTipo.valueOf(pricePolicy.toUpperCase());

        BigDecimal valor;
        switch (tipo){
            case HORA -> {
                long horas = (minutes + 59) / 60;
                if (horas <= 0) horas = 1;
                valor = hourlyRate.multiply(BigDecimal.valueOf(horas));
            }
            case DIARIA -> {
                long dias = (minutes + 1439) / 1440;
                if (dias <= 0) dias = 1;
                valor = dailyRate.multiply(BigDecimal.valueOf(dias));
            }
            case FRACAO_15M -> {
                long fracoes = (minutes + 14) / 15;
                long minFracoes = 4; // mínimo 1h (4 frações)
                if (fracoes < minFracoes) fracoes = minFracoes;
                BigDecimal valorFracao = hourlyRate.divide(BigDecimal.valueOf(4), 2, RoundingMode.HALF_UP);
                valor = valorFracao.multiply(BigDecimal.valueOf(fracoes));
            }
            default -> throw new IllegalArgumentException("Política de preço inválida");
        }
        return valor.setScale(2, RoundingMode.HALF_UP);
    }
}
