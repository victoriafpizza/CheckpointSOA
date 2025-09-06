package br.edu.fiap.soa.estacionamento.web.dto;
import lombok.AllArgsConstructor; import lombok.Data; import java.math.BigDecimal; import java.time.LocalDate;
@Data @AllArgsConstructor
public class FaturamentoDiaResponse { private LocalDate data; private BigDecimal total; }
