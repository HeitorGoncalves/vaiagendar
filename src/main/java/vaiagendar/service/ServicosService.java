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

import vaiagendar.model.Servicos;
import vaiagendar.model.ServicosDTO;
import vaiagendar.model.Logs;
import vaiagendar.model.SearchServicosDTO;
import vaiagendar.repository.ServicosRepository;
import vaiagendar.repository.LogsRepository;

@Service
public class ServicosService {
    

    @Autowired
	private ServicosRepository servicosRepository;

	@Autowired
    private LogsRepository logsRepository;

	@Autowired
	private ModelMapper modelMapper;

	ServicosService(ServicosRepository servicosService) {
		this.servicosRepository = servicosService;
	}


    public List<ServicosDTO> servicosAll() {

		List<Servicos> listServicos = servicosRepository.findAll();

		List<ServicosDTO> listDto = listServicos.stream()
				.map(servicos -> modelMapper.map(servicos, ServicosDTO.class)).collect(Collectors.toList());

		return listDto;
	}


    public Page<Servicos> findAll(Pageable pageable) {

		Page<Servicos> listServicos = servicosRepository.findAll(pageable);

		return listServicos;
	}


    public ServicosDTO findByid(Long id) {

		boolean existe = servicosRepository.existsById(id);
		if (existe == true) {

			Servicos servicos = servicosRepository.getReferenceById(id);
			ServicosDTO servicosDTO = modelMapper.map(servicos, ServicosDTO.class);
			return servicosDTO;
		}
		return new ServicosDTO();

	}


    public Page<Servicos> searchServicos(SearchServicosDTO searchServicosDto, int page, int size, String sortable, String sortableCampo){

		PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.valueOf(sortable), sortableCampo);

    	return servicosRepository.searchServicos(searchServicosDto.getId(), 
      searchServicosDto.getNome().toUpperCase(), pageRequest);
    }


    public boolean deleteById(String userEmail, Long id) {

		boolean existe = servicosRepository.existsById(id);
		if (existe == true) {

			Logs logs = new Logs();
			logs.setAcao("Delete");
			logs.setCadastro("Servicos");
			logs.setConteudo(servicosRepository.getReferenceById(id).toString());
			logs.setCadastroId(id);
			logs.setUpdated(new Date());
			logs.setUsuario(userEmail);

			logsRepository.save(logs);

			servicosRepository.deleteById(id);
			return true;
		}
		return false;
	}


    public ServicosDTO save(String userEmail, ServicosDTO servicosDTO) {

		try {
			Servicos servicos = modelMapper.map(servicosDTO, Servicos.class);						
			servicosRepository.save(servicos);
			servicosDTO.setId(servicos.getId());

			Logs logs = new Logs();
			logs.setAcao("Save");
			logs.setCadastro("Servicos");
			logs.setConteudo(servicosDTO.toString());
			logs.setCadastroId(servicos.getId());
			logs.setUpdated(new Date());
			logs.setUsuario(userEmail);

			logsRepository.save(logs);
			
			return servicosDTO;

		} catch (Exception e) {

			return new ServicosDTO();
		}

	}



    public ServicosDTO update(String userEmail, ServicosDTO servicosDTO) {

		try {
			Servicos servicos = modelMapper.map(servicosDTO, Servicos.class);
			servicosRepository.save(servicos);
			servicosDTO.setId(servicos.getId());

			Logs logs = new Logs();
			logs.setAcao("Update");
			logs.setCadastro("Servicos");
			logs.setConteudo(servicosDTO.toString());
			logs.setCadastroId(servicos.getId());
			logs.setUpdated(new Date());
			logs.setUsuario(userEmail);

			logsRepository.save(logs);

			return servicosDTO;
		} catch (Exception e) {
			return new ServicosDTO();
		}

	}


}
