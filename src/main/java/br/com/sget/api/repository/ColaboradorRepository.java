
package br.com.sget.api.repository;

import br.com.sget.api.model.Colaborador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColaboradorRepository
        extends JpaRepository<Colaborador, Long> {
}