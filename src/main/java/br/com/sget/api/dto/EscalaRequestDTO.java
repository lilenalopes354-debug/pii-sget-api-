
package br.com.sget.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

public class EscalaRequestDTO {

    @NotBlank(message = "O nome da escala é obrigatório")
    @Size(max = 100)
    private String nome;

    @NotNull(message = "A data da escala é obrigatória")
    private LocalDate data;

    @NotNull(message = "A hora de início é obrigatória")
    private LocalTime horaInicio;

    @NotNull(message = "A hora de fim é obrigatória")
    private LocalTime horaFim;

    @Size(max = 500)
    private String observacoes;

    @NotNull(message = "O ID da equipe é obrigatório")
    private Long equipeId;

    private Set<Long> colaboradorIds;

    private String tipoRecorrencia = "PONTUAL";

    private LocalDate dataFimRecorrencia;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(LocalTime horaFim) {
        this.horaFim = horaFim;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Long getEquipeId() {
        return equipeId;
    }

    public void setEquipeId(Long equipeId) {
        this.equipeId = equipeId;
    }

    public Set<Long> getColaboradorIds() {
        return colaboradorIds;
    }

    public void setColaboradorIds(Set<Long> colaboradorIds) {
        this.colaboradorIds = colaboradorIds;
    }

    public String getTipoRecorrencia() {
        return tipoRecorrencia;
    }

    public void setTipoRecorrencia(String tipoRecorrencia) {
        this.tipoRecorrencia = tipoRecorrencia;
    }

    public LocalDate getDataFimRecorrencia() {
        return dataFimRecorrencia;
    }

    public void setDataFimRecorrencia(LocalDate dataFimRecorrencia) {
        this.dataFimRecorrencia = dataFimRecorrencia;
    }
}