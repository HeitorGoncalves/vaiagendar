package vaiagendar.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import vaiagendar.model.Agenda;
import vaiagendar.model.AgendaDTO;
import vaiagendar.model.Logs;
import vaiagendar.model.SearchAgendaDTO;
import vaiagendar.repository.AgendaRepository;
import vaiagendar.repository.LogsRepository;

@Service
public class AgendaService {
    

    @Autowired
	private AgendaRepository agendaRepository;

	@Autowired
    private LogsRepository logsRepository;

	@Autowired
	private ModelMapper modelMapper;

	AgendaService(AgendaRepository agendaService) {
		this.agendaRepository = agendaService;
	}


    public List<AgendaDTO> agendaAll() {

		List<Agenda> listAgenda = agendaRepository.findAll();

		List<AgendaDTO> listDto = listAgenda.stream()
				.map(agenda -> modelMapper.map(agenda, AgendaDTO.class)).collect(Collectors.toList());

		return listDto;
	}


    public Page<Agenda> findAll(Pageable pageable) {

		Page<Agenda> listAgenda = agendaRepository.findAll(pageable);

		return listAgenda;
	}


    public AgendaDTO findByid(Long id) {

		boolean existe = agendaRepository.existsById(id);
		if (existe == true) {

			Agenda agenda = agendaRepository.getReferenceById(id);
			AgendaDTO agendaDTO = modelMapper.map(agenda, AgendaDTO.class);
			return agendaDTO;
		}
		return new AgendaDTO();

	}


    public Page<Agenda> searchAgenda(SearchAgendaDTO searchAgendaDto, int page, int size, String sortable, String sortableCampo){

		PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.valueOf(sortable), sortableCampo);

    	return agendaRepository.searchAgenda(searchAgendaDto.getId(), searchAgendaDto.getNome().toUpperCase(), 
         searchAgendaDto.getWhatsapp().toUpperCase(), searchAgendaDto.getServico(), 
         searchAgendaDto.getDataHora(), pageRequest);
    }


    public boolean deleteById(String userEmail, Long id) {

		boolean existe = agendaRepository.existsById(id);
		if (existe == true) {

			Logs logs = new Logs();
			logs.setAcao("Delete");
			logs.setCadastro("Agenda");
			logs.setConteudo(agendaRepository.getReferenceById(id).toString());
			logs.setCadastroId(id);
			logs.setUpdated(new Date());
			logs.setUsuario(userEmail);

			logsRepository.save(logs);

			agendaRepository.deleteById(id);
			return true;
		}
		return false;
	}


    public AgendaDTO save(String userEmail, AgendaDTO agendaDTO) {

		try {
			Agenda agenda = modelMapper.map(agendaDTO, Agenda.class);						
			agendaRepository.save(agenda);
			agendaDTO.setId(agenda.getId());

			Logs logs = new Logs();
			logs.setAcao("Save");
			logs.setCadastro("Agenda");
			logs.setConteudo(agendaDTO.toString());
			logs.setCadastroId(agenda.getId());
			logs.setUpdated(new Date());
			logs.setUsuario(userEmail);

			logsRepository.save(logs);
			
			return agendaDTO;

		} catch (Exception e) {

			return new AgendaDTO();
		}

	}



    public AgendaDTO update(String userEmail, AgendaDTO agendaDTO) {

		try {
			Agenda agenda = modelMapper.map(agendaDTO, Agenda.class);
			agendaRepository.save(agenda);
			agendaDTO.setId(agenda.getId());

			Logs logs = new Logs();
			logs.setAcao("Update");
			logs.setCadastro("Agenda");
			logs.setConteudo(agendaDTO.toString());
			logs.setCadastroId(agenda.getId());
			logs.setUpdated(new Date());
			logs.setUsuario(userEmail);

			logsRepository.save(logs);

			return agendaDTO;
		} catch (Exception e) {
			return new AgendaDTO();
		}

	}


}
