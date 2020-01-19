package br.charles.controller;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.charles.PageableFactory;
import br.charles.model.Contato;
import br.charles.repository.ContatoRepository;
import br.charles.util.ApiError;
import io.swagger.annotations.ApiOperation;



@RestController
@RequestMapping("/")
public class ContatoController {

	@Autowired
	ContatoRepository contatoRepository;


	
	@GetMapping("/{idContato}")
	public ResponseEntity<Contato> getContato(@PathVariable(value = "idContato") String idContato) {

		String id = ""+idContato;
		Optional<Contato> rastreador = contatoRepository.findById(id);
		
        if (!rastreador.isPresent()) {
            return ApiError.notFound("Contato não encontrado");
        }
		return new ResponseEntity<>(rastreador.get(), HttpStatus.OK);
	}

    
    
	@ApiOperation(value = "Lista os Contatos")
	@GetMapping()
	public Page<Contato> listar(
			@RequestParam(
					value = "size",
					required = false,
					defaultValue = "10") int size,
			@RequestParam(
					value = "page",
					required = false,
					defaultValue = "0") int page
			) {
		Pageable pageable = new PageableFactory(page, size).getPageable();

		Page<Contato> resultPage = null;

		if (pageable != null)
			resultPage = contatoRepository.findAll(pageable);

		return resultPage;
	}
	
	
	@RequestMapping(method = RequestMethod.POST)
	public Contato createContato(@RequestBody Contato contato) {
		return contatoRepository.save(contato);
	}
	
	

}
