package br.edu.fiap.soa.estacionamento.repository;
import br.edu.fiap.soa.estacionamento.domain.Ticket;
import br.edu.fiap.soa.estacionamento.domain.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByStatus(TicketStatus status);
    boolean existsByVeiculoIdAndStatus(Long veiculoId, TicketStatus status);
    long countByStatus(TicketStatus status);
    List<Ticket> findByStatusAndSaidaBetween(TicketStatus status, LocalDateTime de, LocalDateTime ate);
    List<Ticket> findByEntradaBetween(LocalDateTime de, LocalDateTime ate);
}
