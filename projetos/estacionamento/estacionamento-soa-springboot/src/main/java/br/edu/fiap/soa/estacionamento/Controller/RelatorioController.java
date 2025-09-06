package br.edu.fiap.soa.estacionamento.Controller;

import br.edu.fiap.soa.estacionamento.service.RelatorioService;
import br.edu.fiap.soa.estacionamento.web.dto.FaturamentoDiaResponse; import br.edu.fiap.soa.estacionamento.web.dto.UtilizacaoDiaResponse;
import io.swagger.v3.oas.annotations.Operation; import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat; import org.springframework.web.bind.annotation.*;
import java.time.LocalDate; import java.util.List;

@RestController @RequestMapping("/api/relatorios") @Tag(name="Relatórios", description="Faturamento e utilização")
public class RelatorioController {
    private final RelatorioService service;
    public RelatorioController(RelatorioService service){ this.service = service; }

    @GetMapping("faturamento") @Operation(summary="Faturamento por dia no período")
    public List<FaturamentoDiaResponse> faturamento(@RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate de,
                                                    @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate ate){
        return service.faturamento(de, ate);
    }

    @GetMapping("utilizacao") @Operation(summary="Utilização (entradas) por dia no período")
    public List<UtilizacaoDiaResponse> utilizacao(@RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate de,
                                                  @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate ate){
        return service.utilizacao(de, ate);
    }
}
