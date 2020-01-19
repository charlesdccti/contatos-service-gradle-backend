package br.charles.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.charles.PageableFactory;
import br.charles.model.Contato;
import br.charles.repository.ContatoRepository;
import io.swagger.annotations.ApiOperation;



@RestController
@RequestMapping("/")
public class ContatoController {
	
	@Autowired
	ContatoRepository contatoRepository;
	
	//	/**	 
	// 		@GetMapping("/hello")
	//		public String name() {
	//			return "Hello contato";
	//		}
	//	 */
	//	@GetMapping("/")
	//	public List<Contato> findAll() {
	//		return contatoRepository.findAll();
	//	}
	
	   	@ApiOperation(value = "Lista os Contatos")
	    @GetMapping()
	    public Page<Contato> listar(
	            @RequestParam(
	                    value = "page",
	                    required = false,
	                    defaultValue = "0") int page,
	            @RequestParam(
	                    value = "size",
	                    required = false,
	                    defaultValue = "10") int size,
	            @RequestParam(
	                    value = "sort",
	                    required = false) String sort,
	            @RequestParam(
	                    value = "q",
	                    required = false) String q
	    ) {
	        Pageable pageable = new PageableFactory(page, size, sort).getPageable();

	        Page<Contato> resultPage;

	        if (q == null) {
	            resultPage = contatoRepository.findAll(pageable);
	        } else {
	            resultPage = contatoRepository.busca(q.toLowerCase(), pageable);
	        }

	        return resultPage;
	    }

}
