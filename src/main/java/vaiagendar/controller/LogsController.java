package vaiagendar.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vaiagendar.model.Logs;
import vaiagendar.model.LogsDTO;
import vaiagendar.model.SearchLogsDTO;
import vaiagendar.service.LogsService;

@RestController
@RequestMapping({"/logs"})
public class LogsController {
    
    @Autowired
    private LogsService logsService;


    @GetMapping
    public Page<Logs> findAll(Pageable pageable) {

		Page<Logs> listLogsDto = logsService.findAll(pageable);

		return listLogsDto;
	}

    @GetMapping("/logsAll")
	public List<LogsDTO> logsAll() {

		List<LogsDTO> listLogsDto = logsService.logsAll();

		return listLogsDto;
	}


    @GetMapping("/search")
    public Page<Logs> search(
            @RequestParam("searchTerm") String searchTerm,
            @RequestParam(
                    value = "page",
                    required = false,
                    defaultValue = "0") int page,
            @RequestParam(
                    value = "size",
                    required = false,
                    defaultValue = "20") int size) {
        return logsService.search(searchTerm, page, size);

    }

    @PostMapping(value = "/searchLogs/{sortable}/{sortableCampo}")
	public Page<Logs> searchLogs(@RequestBody SearchLogsDTO searchLogsDto, @PathVariable String sortable, @PathVariable String sortableCampo, 
		@RequestParam(
			value = "page",
			required = false,
			defaultValue = "0") int page,
		@RequestParam(
				value = "size",
				required = false,
				defaultValue = "20") int size){			
		
		
		
		return logsService.searchLogs(searchLogsDto, page, size, sortable, sortableCampo);

	}


    @GetMapping(path = "/{id}")
	public ResponseEntity<LogsDTO> findById(@PathVariable Long id) {

		LogsDTO listLogsDto = logsService.findByid(id);

		if (listLogsDto.getId() == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(listLogsDto);
	}

    
}
