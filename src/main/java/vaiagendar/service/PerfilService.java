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

import vaiagendar.model.Perfil;
import vaiagendar.model.PerfilDTO;
import vaiagendar.model.Logs;
import vaiagendar.model.SearchPerfilDTO;
import vaiagendar.repository.PerfilRepository;
import vaiagendar.repository.LogsRepository;

@Service
public class PerfilService {
    

    @Autowired
	private PerfilRepository perfilRepository;

	@Autowired
    private LogsRepository logsRepository;

	@Autowired
	private ModelMapper modelMapper;

	PerfilService(PerfilRepository perfilService) {
		this.perfilRepository = perfilService;
	}


    public List<PerfilDTO> perfilAll() {

		List<Perfil> listPerfil = perfilRepository.findAll();

		List<PerfilDTO> listDto = listPerfil.stream()
				.map(perfil -> modelMapper.map(perfil, PerfilDTO.class)).collect(Collectors.toList());

		return listDto;
	}


    public Page<Perfil> findAll(Pageable pageable) {

		Page<Perfil> listPerfil = perfilRepository.findAll(pageable);

		return listPerfil;
	}


    public PerfilDTO findByid(Long id) {

		boolean existe = perfilRepository.existsById(id);
		if (existe == true) {

			Perfil perfil = perfilRepository.getReferenceById(id);
			PerfilDTO perfilDTO = modelMapper.map(perfil, PerfilDTO.class);
			return perfilDTO;
		}
		return new PerfilDTO();

	}


    public Page<Perfil> searchPerfil(SearchPerfilDTO searchPerfilDto, int page, int size, String sortable, String sortableCampo){

		PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.valueOf(sortable), sortableCampo);

    	return perfilRepository.searchPerfil(searchPerfilDto.getId(), searchPerfilDto.getNome().toUpperCase(), 
         searchPerfilDto.getUsername().toUpperCase(), pageRequest);
    }


    public boolean deleteById(String userEmail, Long id) {

		boolean existe = perfilRepository.existsById(id);
		if (existe == true) {

			Logs logs = new Logs();
			logs.setAcao("Delete");
			logs.setCadastro("Perfil");
			logs.setConteudo(perfilRepository.getReferenceById(id).toString());
			logs.setCadastroId(id);
			logs.setUpdated(new Date());
			logs.setUsuario(userEmail);

			logsRepository.save(logs);

			perfilRepository.deleteById(id);
			return true;
		}
		return false;
	}


    public PerfilDTO save(String userEmail, PerfilDTO perfilDTO) {

		try {
			Perfil perfil = modelMapper.map(perfilDTO, Perfil.class);						
			perfilRepository.save(perfil);
			perfilDTO.setId(perfil.getId());

			Logs logs = new Logs();
			logs.setAcao("Save");
			logs.setCadastro("Perfil");
			logs.setConteudo(perfilDTO.toString());
			logs.setCadastroId(perfil.getId());
			logs.setUpdated(new Date());
			logs.setUsuario(userEmail);

			logsRepository.save(logs);
			
			return perfilDTO;

		} catch (Exception e) {

			return new PerfilDTO();
		}

	}



    public PerfilDTO update(String userEmail, PerfilDTO perfilDTO) {

		try {
			Perfil perfil = modelMapper.map(perfilDTO, Perfil.class);
			perfilRepository.save(perfil);
			perfilDTO.setId(perfil.getId());

			Logs logs = new Logs();
			logs.setAcao("Update");
			logs.setCadastro("Perfil");
			logs.setConteudo(perfilDTO.toString());
			logs.setCadastroId(perfil.getId());
			logs.setUpdated(new Date());
			logs.setUsuario(userEmail);

			logsRepository.save(logs);

			return perfilDTO;
		} catch (Exception e) {
			return new PerfilDTO();
		}

	}


}
