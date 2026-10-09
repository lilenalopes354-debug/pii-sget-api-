package br.com.sget.api.controller;

import br.com.sget.api.dto.EscalaRequestDTO;
import br.com.sget.api.model.Colaborador;
import br.com.sget.api.model.Equipe;
import br.com.sget.api.model.Escala;
import br.com.sget.api.repository.ColaboradorRepository;
import br.com.sget.api.repository.EquipeRepository;
import br.com.sget.api.repository.EscalaRepository;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/escalas")
public class EscalaController {

    private final EscalaRepository escalaRepository;
    private final EquipeRepository equipeRepository;
    private final ColaboradorRepository colaboradorRepository;

    public EscalaController(
            EscalaRepository escalaRepository,
            EquipeRepository equipeRepository,
            ColaboradorRepository colaboradorRepository) {

        this.escalaRepository = escalaRepository;
        this.equipeRepository = equipeRepository;
        this.colaboradorRepository = colaboradorRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Escala criar(@Valid @RequestBody EscalaRequestDTO dados) {

        validarHorarios(dados.getHoraInicio(), dados.getHoraFim());
        validarRecorrencia(
                dados.getTipoRecorrencia(),
                dados.getData(),
                dados.getDataFimRecorrencia()
        );

        Equipe equipe = equipeRepository.findById(dados.getEquipeId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Equipe não encontrada"));

        Escala escala = new Escala();
        preencherDados(escala, dados);
        escala.setEquipe(equipe);
        escala.setColaboradores(
                buscarColaboradores(dados.getColaboradorIds()));

        return escalaRepository.save(escala);
    }

    @GetMapping
    public List<Escala> listar() {
        return escalaRepository.findAll();
    }

    // --- NOVOS ENDPOINTS DE FILTRO ---

    @GetMapping("/periodo")
    public List<Escala> buscarPorPeriodo(
            @RequestParam("inicio") LocalDate inicio,
            @RequestParam("fim") LocalDate fim) {
        return escalaRepository.findByDataBetween(inicio, fim);
    }

    @GetMapping("/equipe/{equipeId}")
    public List<Escala> buscarPorEquipe(@PathVariable Long equipeId) {
        return escalaRepository.findByEquipeId(equipeId);
    }

    @GetMapping("/colaborador/{colaboradorId}")
    public List<Escala> buscarPorColaborador(@PathVariable Long colaboradorId) {
        return escalaRepository.findByColaboradoresId(colaboradorId);
    }

    // ---------------------------------

    @GetMapping("/{id}")
    public Escala buscarPorId(@PathVariable Long id) {
        return escalaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Escala não encontrada"));
    }

    @PutMapping("/{id}")
    public Escala atualizar(
            @PathVariable Long id,
            @Valid @RequestBody EscalaRequestDTO dados) {

        Escala escala = escalaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Escala não encontrada"));

        validarHorarios(dados.getHoraInicio(), dados.getHoraFim());
        validarRecorrencia(
                dados.getTipoRecorrencia(),
                dados.getData(),
                dados.getDataFimRecorrencia()
        );

        Equipe equipe = equipeRepository.findById(dados.getEquipeId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Equipe não encontrada"));

        preencherDados(escala, dados);
        escala.setEquipe(equipe);
        escala.setColaboradores(
                buscarColaboradores(dados.getColaboradorIds()));

        return escalaRepository.save(escala);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {

        if (!escalaRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Escala não encontrada");
        }

        escalaRepository.deleteById(id);
    }

    private void preencherDados(
            Escala escala,
            EscalaRequestDTO dados) {

        escala.setNome(dados.getNome());
        escala.setData(dados.getData());
        escala.setHoraInicio(dados.getHoraInicio());
        escala.setHoraFim(dados.getHoraFim());
        escala.setObservacoes(dados.getObservacoes());

        escala.setTipoRecorrencia(
                dados.getTipoRecorrencia() == null
                        || dados.getTipoRecorrencia().isBlank()
                        ? "PONTUAL"
                        : dados.getTipoRecorrencia().toUpperCase()
        );

        escala.setDataFimRecorrencia(
                dados.getDataFimRecorrencia());
    }

    private void validarHorarios(
            java.time.LocalTime horaInicio,
            java.time.LocalTime horaFim) {

        if (horaInicio == null || horaFim == null
                || !horaFim.isAfter(horaInicio)) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A hora de fim deve ser posterior à hora de início");
        }
    }

    private void validarRecorrencia(
            String tipo,
            java.time.LocalDate data,
            java.time.LocalDate dataFim) {

        String tipoNormalizado = tipo == null || tipo.isBlank()
                ? "PONTUAL"
                : tipo.toUpperCase();

        if (!Set.of("PONTUAL", "DIARIA", "SEMANAL", "MENSAL")
                .contains(tipoNormalizado)) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Tipo de recorrência inválido");
        }

        if (!"PONTUAL".equals(tipoNormalizado)) {

            if (dataFim == null || dataFim.isBefore(data)) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "A data final da recorrência deve ser igual ou posterior à data da escala");
            }
        }
    }

    private Set<Colaborador> buscarColaboradores(
            Set<Long> colaboradorIds) {

        if (colaboradorIds == null || colaboradorIds.isEmpty()) {
            return new HashSet<>();
        }

        List<Colaborador> encontrados =
                colaboradorRepository.findAllById(colaboradorIds);

        if (encontrados.size() != colaboradorIds.size()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Um ou mais colaboradores não foram encontrados");
        }

        return new HashSet<>(encontrados);
    }
}