package com.example.DoencaChagas.repository;

import com.example.DoencaChagas.model.Paciente;
import com.example.DoencaChagas.model.Cidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface BancoDeDadosDAO extends JpaRepository<Paciente, Integer> {
    
    // Método que adiciona um novo registro ao repository
    default Paciente adicionarNovaOcorrencia(Paciente paciente) {
        return this.save(paciente);
    }

    @Query("SELECT c FROM Cidade c WHERE c.idCidade = :idCidade")
    Cidade findCidadeById(@Param("idCidade") Integer idCidade);

    List<Paciente> findByCidade_IdCidade(Integer idCidade, Pageable pageable);
    
    List<Paciente> findByCidade_IdCidade(Integer idCidade);

    @Query("SELECT c FROM Cidade c")
    List<Cidade> findAllCidades();

    List<Paciente> findByCidade_IdCidadeAndDataRegistroBetween(Integer idCidade, java.time.LocalDate dataInicial, java.time.LocalDate dataFinal);
}
