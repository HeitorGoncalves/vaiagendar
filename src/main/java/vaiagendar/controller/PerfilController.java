package vaiagendar.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import vaiagendar.model.Perfil;
import vaiagendar.model.PerfilDTO;
import vaiagendar.model.SearchPerfilDTO;
import vaiagendar.service.PerfilService;

@RestController
@RequestMapping({ "/perfil" })
@Validated
public class PerfilController {
    

    @Autowired
	private PerfilService perfilService;


    @GetMapping("/perfilAll")
	public List<PerfilDTO> perfilAll() {

		List<PerfilDTO> listPerfilDTO = perfilService.perfilAll();

		return listPerfilDTO;
	}

    @GetMapping
	public Page<Perfil> findAll(Pageable pageable) {

		Page<Perfil> listPerfil = perfilService.findAll(pageable);

		return listPerfil;
	}


    @GetMapping(path = "/{id}")
	public ResponseEntity<PerfilDTO> findById(@PathVariable Long id) {

		PerfilDTO listPerfilDTO = perfilService.findByid(id);

		if (listPerfilDTO.getId() == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(listPerfilDTO);
	}


    @PostMapping(value = "/searchPerfil/{sortable}/{sortableCampo}")
	public Page<Perfil> searchPerfil(@RequestBody SearchPerfilDTO searchPerfilDTO,
			@PathVariable String sortable, @PathVariable String sortableCampo,
			@RequestParam(value = "page", required = false, defaultValue = "0") int page,
			@RequestParam(value = "size", required = false, defaultValue = "20") int size) {

		System.out.println("---------------------------------------------------------------------------------");
		System.out.println(searchPerfilDTO);

		return perfilService.searchPerfil(searchPerfilDTO, page, size, sortable, sortableCampo);

	}


    @DeleteMapping(path = "/{id}")
	public ResponseEntity<String> deleteById(@RequestHeader Map<String, String> headers, @PathVariable Long id) {

		boolean sucesso = perfilService.deleteById(headers.get("email"), id);
		if (sucesso == true) {
			return ResponseEntity.ok("{Registro Deletado}");
		}
		return ResponseEntity.badRequest().body("Registro contêm vínculos, não pode ser deletado");

	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleException(MethodArgumentNotValidException e) {

		List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
		Map<String, String> response = new HashMap<>();

		allErrors.forEach(v -> {
			String fieldName = ((FieldError) v).getField();
			response.put(fieldName, v.getDefaultMessage());
		});

		return response;

	}


    @PostMapping
	public ResponseEntity<PerfilDTO> save(@RequestHeader Map<String, String> headers,
		@Valid @RequestBody PerfilDTO perfilDTO) {

		perfilDTO = perfilService.save(headers.get("email"), perfilDTO);

		if (perfilDTO.getId() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(perfilDTO);

	}


    @PutMapping
	public ResponseEntity<PerfilDTO> update(@RequestHeader Map<String, String> headers,
		@Valid @RequestBody PerfilDTO perfilDTO) {

		perfilDTO = perfilService.update(headers.get("email"), perfilDTO);

		if (perfilDTO.getId() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(perfilDTO);

	}
}
