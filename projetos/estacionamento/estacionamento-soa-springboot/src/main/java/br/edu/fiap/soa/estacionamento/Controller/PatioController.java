package br.edu.fiap.soa.estacionamento.Controller;

import br.edu.fiap.soa.estacionamento.service.OcupacaoService;
import io.swagger.v3.oas.annotations.Operation; import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*; import java.util.Map;

@RestController @RequestMapping("/api/patio") @Tag(name="Pátio", description="Lotação e disponibilidade")
public class PatioController {
    private final OcupacaoService ocupacao;
    public PatioController(OcupacaoService ocupacao){ this.ocupacao = ocupacao; }

    @GetMapping("status") @Operation(summary="Status do pátio (capacidade/ocupação)")
    public Map<String,Object> status(){
        var s = ocupacao.status();
        return Map.of("capacidade", s.capacidade(), "ocupados", s.ocupados(), "disponiveis", s.disponiveis());
    }
}
