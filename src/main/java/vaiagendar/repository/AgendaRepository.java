package vaiagendar.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

import vaiagendar.model.Agenda;

@Repository
public interface AgendaRepository extends JpaRepository<Agenda, Long> {
    
    @Query("FROM Agenda a WHERE (:id is null or a.id = :id) AND (:nome is '' or UPPER(a.nome) like %:nome%) " +
    "AND (:whatsapp is '' or UPPER(a.whatsapp) like %:whatsapp%) AND (:servico is null or a.servico = :servico) " +
    " AND (:dataHora is null or a.dataHora = :dataHora)")
    Page<Agenda> searchAgenda(@Param("id") Long id, @Param("nome") String nome, 
    @Param("whatsapp") String whatsapp, @Param("servico") Long servico, 
    @Param("dataHora") Date dataHora, Pageable pageable);

}
