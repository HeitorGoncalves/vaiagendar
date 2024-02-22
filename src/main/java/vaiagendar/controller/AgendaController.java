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

import vaiagendar.model.Agenda;
import vaiagendar.model.AgendaDTO;
import vaiagendar.model.SearchAgendaDTO;
import vaiagendar.service.AgendaService;

@RestController
@RequestMapping({ "/agenda" })
@Validated
public class AgendaController {
    

    @Autowired
	private AgendaService agendaService;


    @GetMapping("/agendaAll")
	public List<AgendaDTO> agendaAll() {

		List<AgendaDTO> listAgendaDTO = agendaService.agendaAll();

		return listAgendaDTO;
	}

    @GetMapping
	public Page<Agenda> findAll(Pageable pageable) {

		Page<Agenda> listAgenda = agendaService.findAll(pageable);

		return listAgenda;
	}


    @GetMapping(path = "/{id}")
	public ResponseEntity<AgendaDTO> findById(@PathVariable Long id) {

		AgendaDTO listAgendaDTO = agendaService.findByid(id);

		if (listAgendaDTO.getId() == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(listAgendaDTO);
	}


    @PostMapping(value = "/searchAgenda/{sortable}/{sortableCampo}")
	public Page<Agenda> searchAgenda(@RequestBody SearchAgendaDTO searchAgendaDTO,
			@PathVariable String sortable, @PathVariable String sortableCampo,
			@RequestParam(value = "page", required = false, defaultValue = "0") int page,
			@RequestParam(value = "size", required = false, defaultValue = "20") int size) {

		System.out.println("---------------------------------------------------------------------------------");
		System.out.println(searchAgendaDTO);

		return agendaService.searchAgenda(searchAgendaDTO, page, size, sortable, sortableCampo);

	}


    @DeleteMapping(path = "/{id}")
	public ResponseEntity<String> deleteById(@RequestHeader Map<String, String> headers, @PathVariable Long id) {

		boolean sucesso = agendaService.deleteById(headers.get("email"), id);
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
	public ResponseEntity<AgendaDTO> save(@RequestHeader Map<String, String> headers,
		@Valid @RequestBody AgendaDTO agendaDTO) {

	  agendaDTO = agendaService.save(headers.get("email"), agendaDTO);

		if (agendaDTO.getId() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(agendaDTO);

	}


    @PutMapping
	public ResponseEntity<AgendaDTO> update(@RequestHeader Map<String, String> headers,
		@Valid @RequestBody AgendaDTO agendaDTO) {

		agendaDTO = agendaService.update(headers.get("email"), agendaDTO);

		if (agendaDTO.getId() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(agendaDTO);

	}
}
