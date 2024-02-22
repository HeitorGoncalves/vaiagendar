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

import vaiagendar.model.Servicos;
import vaiagendar.model.ServicosDTO;
import vaiagendar.model.SearchServicosDTO;
import vaiagendar.service.ServicosService;

@RestController
@RequestMapping({ "/servicos" })
@Validated
public class ServicosController {
    

    @Autowired
	private ServicosService servicosService;


    @GetMapping("/servicosAll")
	public List<ServicosDTO> servicosAll() {

		List<ServicosDTO> listServicosDTO = servicosService.servicosAll();

		return listServicosDTO;
	}

    @GetMapping
	public Page<Servicos> findAll(Pageable pageable) {

		Page<Servicos> listServicos = servicosService.findAll(pageable);

		return listServicos;
	}


    @GetMapping(path = "/{id}")
	public ResponseEntity<ServicosDTO> findById(@PathVariable Long id) {

		ServicosDTO listServicosDTO = servicosService.findByid(id);

		if (listServicosDTO.getId() == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(listServicosDTO);
	}


    @PostMapping(value = "/searchServicos/{sortable}/{sortableCampo}")
	public Page<Servicos> searchServicos(@RequestBody SearchServicosDTO searchServicosDTO,
			@PathVariable String sortable, @PathVariable String sortableCampo,
			@RequestParam(value = "page", required = false, defaultValue = "0") int page,
			@RequestParam(value = "size", required = false, defaultValue = "20") int size) {

		System.out.println("---------------------------------------------------------------------------------");
		System.out.println(searchServicosDTO);

		return servicosService.searchServicos(searchServicosDTO, page, size, sortable, sortableCampo);

	}


    @DeleteMapping(path = "/{id}")
	public ResponseEntity<String> deleteById(@RequestHeader Map<String, String> headers, @PathVariable Long id) {

		boolean sucesso = servicosService.deleteById(headers.get("email"), id);
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
	public ResponseEntity<ServicosDTO> save(@RequestHeader Map<String, String> headers,
		@Valid @RequestBody ServicosDTO servicosDTO) {

		servicosDTO = servicosService.save(headers.get("email"), servicosDTO);

		if (servicosDTO.getId() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(servicosDTO);

	}


    @PutMapping
	public ResponseEntity<ServicosDTO> update(@RequestHeader Map<String, String> headers,
		@Valid @RequestBody ServicosDTO servicosDTO) {

		servicosDTO = servicosService.update(headers.get("email"), servicosDTO);

		if (servicosDTO.getId() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(servicosDTO);

	}
}
