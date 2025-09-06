package br.edu.fiap.soa.estacionamento.service;

import br.edu.fiap.soa.estacionamento.domain.Ticket; import br.edu.fiap.soa.estacionamento.domain.TicketStatus;
import br.edu.fiap.soa.estacionamento.repository.TicketRepository;
import br.edu.fiap.soa.estacionamento.web.dto.FaturamentoDiaResponse; import br.edu.fiap.soa.estacionamento.web.dto.UtilizacaoDiaResponse;
import org.springframework.stereotype.Service;
import java.math.BigDecimal; import java.time.*; import java.util.*; import java.util.stream.Collectors;

@Service
public class RelatorioService {
    private final TicketRepository repo;
    public RelatorioService(TicketRepository repo){ this.repo = repo; }

    public List<FaturamentoDiaResponse> faturamento(LocalDate de, LocalDate ate){
        LocalDateTime ini = de.atStartOfDay();
        LocalDateTime fim = ate.plusDays(1).atStartOfDay().minusNanos(1);
        List<Ticket> fechados = repo.findByStatusAndSaidaBetween(TicketStatus.FECHADO, ini, fim);
        Map<LocalDate, BigDecimal> mapa = new TreeMap<>();
        for (Ticket t : fechados){
            LocalDate dia = t.getSaida().toLocalDate();
            mapa.merge(dia, t.getValor()==null?BigDecimal.ZERO:t.getValor(), BigDecimal::add);
        }
        return mapa.entrySet().stream().map(e -> new FaturamentoDiaResponse(e.getKey(), e.getValue())).collect(Collectors.toList());
    }

    public List<UtilizacaoDiaResponse> utilizacao(LocalDate de, LocalDate ate){
        LocalDateTime ini = de.atStartOfDay();
        LocalDateTime fim = ate.plusDays(1).atStartOfDay().minusNanos(1);
        List<Ticket> entradas = repo.findByEntradaBetween(ini, fim);
        Map<LocalDate, Long> mapa = new TreeMap<>();
        for (Ticket t : entradas){
            LocalDate dia = t.getEntrada().toLocalDate();
            mapa.merge(dia, 1L, Long::sum);
        }
        return mapa.entrySet().stream().map(e -> new UtilizacaoDiaResponse(e.getKey(), e.getValue())).collect(Collectors.toList());
    }
}
