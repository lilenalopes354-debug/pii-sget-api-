package br.com.sget.api.repository;

import br.com.sget.api.model.Escala;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface EscalaRepository extends JpaRepository<Escala, Long> {

    // Buscar escalas em um intervalo de datas (útil para calendários semanais/mensais)
    List<Escala> findByDataBetween(LocalDate dataInicio, LocalDate dataFim);

    // Buscar escalas vinculadas a uma equipe específica
    List<Escala> findByEquipeId(Long equipeId);

    // Buscar todas as escalas que contêm um colaborador específico
    List<Escala> findByColaboradoresId(Long colaboradorId);
}