
package br.com.sget.api.controller;

import br.com.sget.api.model.Colaborador;
import br.com.sget.api.repository.ColaboradorRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/colaboradores")
public class ColaboradorController {

    private final ColaboradorRepository colaboradorRepository;

    public ColaboradorController(
            ColaboradorRepository colaboradorRepository) {
        this.colaboradorRepository = colaboradorRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Colaborador criar(
            @Valid @RequestBody Colaborador colaborador) {
        return colaboradorRepository.save(colaborador);
    }

    @GetMapping
    public List<Colaborador> listar() {
        return colaboradorRepository.findAll();
    }

    @GetMapping("/{id}")
    public Colaborador buscarPorId(@PathVariable Long id) {
        return colaboradorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Colaborador não encontrado"));
    }

    @PutMapping("/{id}")
    public Colaborador atualizar(
            @PathVariable Long id,
            @Valid @RequestBody Colaborador dados) {

        Colaborador colaborador = colaboradorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Colaborador não encontrado"));

        colaborador.setNome(dados.getNome());
        colaborador.setEmail(dados.getEmail());
        colaborador.setTelefone(dados.getTelefone());

        return colaboradorRepository.save(colaborador);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        if (!colaboradorRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Colaborador não encontrado");
        }

        colaboradorRepository.deleteById(id);
    }
}