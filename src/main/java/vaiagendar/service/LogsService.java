package vaiagendar.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import vaiagendar.model.Logs;
import vaiagendar.model.LogsDTO;
import vaiagendar.model.SearchLogsDTO;
import vaiagendar.repository.LogsRepository;

@Service
public class LogsService {
    

    @Autowired
    private LogsRepository logsRepository;

    @Autowired
    private ModelMapper modelMapper;

    LogsService(LogsRepository logsService) {
		this.logsRepository = logsService;
	}

    public List<LogsDTO> logsAll() {
       
		List<Logs> listLogs = logsRepository.findAll();

		List<LogsDTO> listDto = listLogs.stream()
				.map(logs -> modelMapper.map(logs, LogsDTO.class)).collect(Collectors.toList());

		return listDto;
	}

	public Page<Logs> findAll(Pageable pageable) {			
		return logsRepository.findAll(pageable);
	}

	public Page<Logs> search(String searchTerm, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.ASC, "usuario");
        return logsRepository.search(searchTerm, pageRequest);
    }


	public Page<Logs> searchLogs(SearchLogsDTO searchLogsDto, int page, int size, String sortable, String sortableCampo){

		PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.valueOf(sortable), sortableCampo);

    	return logsRepository.searchLogs(searchLogsDto.getId(), searchLogsDto.getUsuario().toUpperCase(), 
		searchLogsDto.getCadastroId(), searchLogsDto.getCadastro().toUpperCase(), searchLogsDto.getAcao().toUpperCase(), pageRequest);
    }
	


	public LogsDTO findByid(Long id) {

		boolean existe = logsRepository.existsById(id);
		if (existe == true) {
			Logs logs = logsRepository.getReferenceById(id);
			LogsDTO logsDTO = modelMapper.map(logs, LogsDTO.class);
			return logsDTO;
		}
		return new LogsDTO();

	}

	public LogsDTO save(LogsDTO logsDTO) {

		try {
			Logs logs = modelMapper.map(logsDTO, Logs.class);
			Long id = logsRepository.save(logs).getId();
			logsDTO.setId(id);
			return logsDTO;

		} catch (Exception e) {
			return new LogsDTO();
		}
	}


    
}
