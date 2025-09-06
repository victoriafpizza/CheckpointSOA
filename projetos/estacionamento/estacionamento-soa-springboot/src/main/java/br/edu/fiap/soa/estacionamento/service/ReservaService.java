package br.edu.fiap.soa.estacionamento.service;

import br.edu.fiap.soa.estacionamento.domain.*; import br.edu.fiap.soa.estacionamento.repository.ReservaRepository;
import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime; import java.util.*;

@Service
public class ReservaService {
    private final ReservaRepository repo;
    public ReservaService(ReservaRepository repo){ this.repo = repo; }

    public List<Reserva> ativas(){ return repo.findByStatus(ReservaStatus.ATIVA); }

    @Transactional public Reserva criar(String placa, String vaga, LocalDateTime inicio, LocalDateTime fim){
        if (fim.isBefore(inicio)) throw new RuntimeException("Período inválido");
        Reserva r = Reserva.builder().placa(placa).vaga(vaga).inicioPrevisto(inicio).fimPrevisto(fim).status(ReservaStatus.ATIVA).build();
        return repo.save(r);
    }

    @Transactional public void cancelar(Long id){
        Reserva r = repo.findById(id).orElseThrow(()-> new RuntimeException("Reserva não encontrada"));
        r.setStatus(ReservaStatus.CANCELADA);
    }

    public Optional<Reserva> reservaAtivaNoMomento(String placa, String vaga, LocalDateTime agora){
        Optional<Reserva> r = repo.findFirstByPlacaAndStatusAndInicioPrevistoLessThanEqualAndFimPrevistoGreaterThanEqual(
                placa, ReservaStatus.ATIVA, agora, agora);
        if (r.isPresent() && vaga != null && !vaga.isBlank()){
            return r.filter(res -> res.getVaga()==null || vaga.equalsIgnoreCase(res.getVaga()));
        }
        return r;
    }

    @Transactional public void marcarCumprida(Long id){
        Reserva r = repo.findById(id).orElseThrow(()-> new RuntimeException("Reserva não encontrada"));
        r.setStatus(ReservaStatus.CUMPRIDA);
    }
}
