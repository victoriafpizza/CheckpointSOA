package br.edu.fiap.soa.estacionamento.Controller;

import br.edu.fiap.soa.estacionamento.domain.Reserva;
import br.edu.fiap.soa.estacionamento.service.ReservaService;
import br.edu.fiap.soa.estacionamento.web.dto.ReservaRequest; import br.edu.fiap.soa.estacionamento.web.dto.ReservaResponse;
import io.swagger.v3.oas.annotations.Operation; import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid; import org.springframework.http.ResponseEntity; import org.springframework.web.bind.annotation.*;
import java.net.URI; import java.util.List;

@RestController @RequestMapping("/api/reservas") @Tag(name="Reservas", description="Reservas de vaga")
public class ReservaController {
    private final ReservaService service;
    public ReservaController(ReservaService service){ this.service = service; }

    @GetMapping("ativas") @Operation(summary="Listar reservas ativas")
    public List<ReservaResponse> ativas(){
        return service.ativas().stream().map(this::toDto).toList();
    }

    @PostMapping @Operation(summary="Criar reserva")
    public ResponseEntity<ReservaResponse> criar(@RequestBody @Valid ReservaRequest req){
        Reserva r = service.criar(req.getPlaca(), req.getVaga(), req.getInicioPrevisto(), req.getFimPrevisto());
        return ResponseEntity.created(URI.create("/api/reservas/"+r.getId())).body(toDto(r));
    }

    @PostMapping("{id}/cancelar") @Operation(summary="Cancelar reserva")
    public ResponseEntity<Void> cancelar(@PathVariable Long id){
        service.cancelar(id);
        return ResponseEntity.noContent().build();
    }

    private ReservaResponse toDto(Reserva r){
        return ReservaResponse.builder()
                .id(r.getId()).placa(r.getPlaca()).vaga(r.getVaga())
                .inicioPrevisto(r.getInicioPrevisto()).fimPrevisto(r.getFimPrevisto())
                .status(r.getStatus()).build();
    }
}
