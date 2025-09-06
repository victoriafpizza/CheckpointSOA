package br.edu.fiap.soa.estacionamento.repository;

import br.edu.fiap.soa.estacionamento.domain.Reserva;
import br.edu.fiap.soa.estacionamento.domain.ReservaStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime; import java.util.*;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByStatus(ReservaStatus status);
    Optional<Reserva> findFirstByPlacaAndStatusAndInicioPrevistoLessThanEqualAndFimPrevistoGreaterThanEqual(
            String placa, ReservaStatus status, LocalDateTime agora1, LocalDateTime agora2);
}
