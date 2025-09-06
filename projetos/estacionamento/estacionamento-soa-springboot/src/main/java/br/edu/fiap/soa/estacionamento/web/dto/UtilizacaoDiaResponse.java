package br.edu.fiap.soa.estacionamento.web.dto;
import lombok.AllArgsConstructor; import lombok.Data; import java.time.LocalDate;
@Data @AllArgsConstructor
public class UtilizacaoDiaResponse { private LocalDate data; private long tickets; }
