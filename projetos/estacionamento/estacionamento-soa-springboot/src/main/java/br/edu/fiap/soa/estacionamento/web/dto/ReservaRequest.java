package br.edu.fiap.soa.estacionamento.web.dto;
import io.swagger.v3.oas.annotations.media.Schema; import jakarta.validation.constraints.*; import lombok.Data;
import java.time.LocalDateTime;
@Data
public class ReservaRequest {
    @Schema(example="ABC-1D23") @NotBlank private String placa;
    @Schema(example="A12") private String vaga;
    @NotNull private LocalDateTime inicioPrevisto;
    @NotNull private LocalDateTime fimPrevisto;
}
