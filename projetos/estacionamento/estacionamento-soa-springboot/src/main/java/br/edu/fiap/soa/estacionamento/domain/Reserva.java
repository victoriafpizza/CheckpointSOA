package br.edu.fiap.soa.estacionamento.domain;

import jakarta.persistence.*; import lombok.*; import java.time.LocalDateTime;

@Entity @Table(name="reserva")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Reserva {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length=10, nullable=false) private String placa;
    @Column(length=20) private String vaga;

    @Column(nullable=false) private LocalDateTime inicioPrevisto;
    @Column(nullable=false) private LocalDateTime fimPrevisto;

    @Enumerated(EnumType.STRING) @Column(length=20, nullable=false)
    private ReservaStatus status;

    @PrePersist void pre(){ if(status==null) status = ReservaStatus.ATIVA; }
}
