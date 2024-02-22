package vaiagendar.model;

import java.util.Date;

import lombok.Data;

@Data
public class SearchAgendaDTO {
    
    private Long id;

    private String nome;

    private String whatsapp;

    private Long servico;

    private Date dataHora;
    
}
