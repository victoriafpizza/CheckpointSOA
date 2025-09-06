package br.edu.fiap.soa.estacionamento.service;

import br.edu.fiap.soa.estacionamento.domain.TicketStatus;
import br.edu.fiap.soa.estacionamento.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OcupacaoService {
    private final TicketRepository ticketRepo;
    @Value("${parking.capacity:50}") private int capacidade;

    public OcupacaoService(TicketRepository ticketRepo){ this.ticketRepo = ticketRepo; }

    public record PatioStatus(int capacidade, long ocupados, long disponiveis) {}

    public PatioStatus status(){
        long ocupados = ticketRepo.countByStatus(TicketStatus.ABERTO);
        long disponiveis = Math.max(0, capacidade - ocupados);
        return new PatioStatus(capacidade, ocupados, disponiveis);
    }

    public void validarDisponibilidade(){
        if (ticketRepo.countByStatus(TicketStatus.ABERTO) >= capacidade){
            throw new RuntimeException("Pátio lotado");
        }
    }
}
