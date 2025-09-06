package br.edu.fiap.soa.estacionamento;

import br.edu.fiap.soa.estacionamento.service.PrecoService;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TicketServiceTest {

    private static void setField(Object target, String fieldName, Object value) {
        try {
            Field f = PrecoService.class.getDeclaredField(fieldName);
            f.setAccessible(true);
            f.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void calcula_valor_minimo_1h() {
        PrecoService preco = new PrecoService();
        setField(preco, "hourlyRate", new BigDecimal("10.00"));
        setField(preco, "dailyRate", new BigDecimal("60.00"));
        setField(preco, "pricePolicy", "HORA");

        LocalDateTime t = LocalDateTime.now();
        assertEquals(new BigDecimal("10.00"), preco.calcular(t, t));
    }

    @Test
    void arredonda_para_cima() {
        PrecoService preco = new PrecoService();
        setField(preco, "hourlyRate", new BigDecimal("8.00"));
        setField(preco, "dailyRate", new BigDecimal("60.00"));
        setField(preco, "pricePolicy", "HORA");

        LocalDateTime now = LocalDateTime.now();
        assertEquals(new BigDecimal("8.00"),  preco.calcular(now.minusMinutes(30), now)); // 30m -> 1h
        assertEquals(new BigDecimal("16.00"), preco.calcular(now.minusMinutes(61), now)); // 61m -> 2h
    }
}
