package br.edu.fiap.soa.estacionamento.service;

import br.edu.fiap.soa.estacionamento.domain.Ticket;
import br.edu.fiap.soa.estacionamento.domain.TicketStatus;
import br.edu.fiap.soa.estacionamento.domain.Veiculo;
import br.edu.fiap.soa.estacionamento.repository.TicketRepository;
import br.edu.fiap.soa.estacionamento.repository.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepo;
    private final VeiculoRepository veiculoRepo;
    private final PrecoService preco;           // pode ser null no construtor legado
    private final OcupacaoService ocupacao;     // pode ser null no construtor legado
    private final ReservaService reservaService; // pode ser null no construtor legado

    // Construtor principal (usado pelo Spring)
    @Autowired
    public TicketService(TicketRepository t, VeiculoRepository v, PrecoService p, OcupacaoService o, ReservaService r) {
        this.ticketRepo = t;
        this.veiculoRepo = v;
        this.preco = p;
        this.ocupacao = o;
        this.reservaService = r;
    }

    // Construtor legado (compatível com testes/código antigo que passavam só 2 deps)
    public TicketService(TicketRepository t, VeiculoRepository v) {
        this(t, v, null, null, null);
    }

    public Ticket obter(Long id) {
        return ticketRepo.findById(id).orElseThrow(() -> new RuntimeException("Ticket não encontrado"));
    }

    public List<Ticket> listarAbertos() {
        return ticketRepo.findByStatus(TicketStatus.ABERTO);
    }

    @Transactional
    public Ticket checkIn(String placa, String modelo, String cor, String vaga) {
        if (ocupacao != null) {
            ocupacao.validarDisponibilidade();
        }

        Veiculo veiculo = veiculoRepo.findByPlaca(placa).orElseGet(() -> {
            Veiculo nv = new Veiculo();
            nv.setPlaca(placa);
            nv.setModelo(modelo);
            nv.setCor(cor);
            return veiculoRepo.save(nv);
        });

        if (ticketRepo.existsByVeiculoIdAndStatus(veiculo.getId(), TicketStatus.ABERTO)) {
            throw new RuntimeException("Já existe um ticket ABERTO para este veículo");
        }

        var agora = LocalDateTime.now();

        if (reservaService != null) {
            var optReserva = reservaService.reservaAtivaNoMomento(placa, vaga, agora);
            if (optReserva.isPresent()) {
                var r = optReserva.get();
                if (vaga == null || vaga.isBlank()) vaga = r.getVaga();
                reservaService.marcarCumprida(r.getId());
            }
        }

        Ticket t = Ticket.builder()
                .veiculo(veiculo)
                .vaga(vaga)
                .entrada(agora)
                .status(TicketStatus.ABERTO)
                .build();

        return ticketRepo.save(t);
    }

    @Transactional
    public Ticket checkOut(Long ticketId) {
        Ticket t = obter(ticketId);
        if (t.getStatus() == TicketStatus.FECHADO) {
            throw new RuntimeException("Ticket já está fechado");
        }

        LocalDateTime agora = LocalDateTime.now();

        BigDecimal valor;
        if (preco != null) {
            // cálculo via política configurável
            valor = preco.calcular(t.getEntrada(), agora);
        } else {
            // fallback legado: arredondar para cima por hora, mínimo 1h, tarifa 8.00
            long minutes = Duration.between(t.getEntrada(), agora).toMinutes();
            long hours = (minutes + 59) / 60;
            if (hours <= 0) hours = 1;
            valor = new BigDecimal("8.00").multiply(BigDecimal.valueOf(hours));
        }

        t.setSaida(agora);
        t.setValor(valor);
        t.setStatus(TicketStatus.FECHADO);

        return ticketRepo.save(t);
    }
}
