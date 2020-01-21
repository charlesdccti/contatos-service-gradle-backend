package br.charles.controller;


import java.util.Date;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.charles.PageableFactory;
import br.charles.exception.ContatoNotFoundException;
import br.charles.exception.ExceptionResponse;
import br.charles.model.Contato;
import br.charles.repository.ContatoRepository;
import br.charles.util.ApiHttpStatus;
import io.swagger.annotations.ApiOperation;
import javassist.NotFoundException;



@RestController
@RequestMapping("/")
public class ContatoController {

	@Autowired
	ContatoRepository contatoRepository;

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());




	@GetMapping("/{idContato}")
	public ResponseEntity getContato(@PathVariable(value = "idContato") Integer idContato) {
		
		Optional<Contato> rastreador = contatoRepository.findById(String.valueOf(idContato));
		
		if (!rastreador.isPresent()) {
			throw new ContatoNotFoundException("Contato não encontrado para o id = " + idContato);
			//return ApiError.notFound("Contato não encontrado");
		}
		
		return new ResponseEntity<>(rastreador.get(), HttpStatus.OK);
	}



	@PutMapping("/{idContato}")
	public ResponseEntity updateContato(@PathVariable(value = "idContato") Integer idContato, @Valid @RequestBody Contato dto) {

		try {
			Optional<Contato> contatoAtual = contatoRepository.findById(String.valueOf(idContato));

			if (!contatoAtual.isPresent()) {
				throw new ContatoNotFoundException("Contato não encontrado para o id:" + idContato);
				//return ApiError.notFound("Contato não encontrado");
			}

			Contato contatoAtualizado = contatoRepository.save(contatoAtual.get());

			if (contatoAtualizado == null) {
				return ApiHttpStatus.internalServerError("Erro na atualização do contato");
			}

			LOGGER.info("Contato "+contatoAtualizado.getNome()+" foi atualizado com sucesso!!");
			return new ResponseEntity<>(contatoAtualizado, HttpStatus.NO_CONTENT);

		} catch (Exception e) {
			LOGGER.error("Erro ao atualizar um Contato", e);
			return ApiHttpStatus.internalServerError("Erro na atualização do contato");
		}
	}





	@DeleteMapping("/{idContato}")	
	@ApiOperation(value = "Apaga um objeto do tipo Contato")
	public ResponseEntity<Object> deleteContato(@PathVariable(value = "idContato") Integer idContato) {

		Optional<Contato> contato = contatoRepository.findById(String.valueOf(idContato));

		if (!contato.isPresent()) {
			throw new ContatoNotFoundException("Contato não encontrado para o id:" + idContato);
			//return ApiError.notFound("Contato não encontrado");
		} else {
			contatoRepository.deleteById(String.valueOf(idContato));
		}

		LOGGER.info("Contato foi removido com sucesso!!");
		return ApiHttpStatus.noContetMessage("Contato foi removido com sucesso!!");
		//new ExceptionResponse(new Date(), HttpStatus., "Contato foi removido com sucesso!!");
		//return new ResponseEntity<>(new ExceptionResponse(new Date(),HttpStatus.NO_CONTENT,"Contato foi removido com sucesso!!", "Gloria Deus!!"), HttpStatus.NO_CONTENT);
	}




	@GetMapping("/")
	@ApiOperation(value = "Lista os Contatos")
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

		LOGGER.info("A lista de contatos com parginação foi retornada com sucesso!!");
		return resultPage;
	}



	@PostMapping("/")
	@ApiOperation(value = "Cria um novo Contato")
	public ResponseEntity postContato(@Valid @RequestBody Contato dto) {

		try {
			Contato contato = new Contato();
			contato.setIdContato(dto.getIdContato());
			contato.setNome(dto.getNome());
			contato.setCanal(dto.getCanal());
			contato.setValor(dto.getValor());
			contato.setObs(dto.getObs());

			Contato novo = contatoRepository.save(contato);

			//Se ocorreu algum erro, retorno esse erro para a API
			if (novo == null) {
				return ApiHttpStatus.badRequest("Ocorreu algum erro na criação do contato");
			}

			LOGGER.info("Contato foi criado com sucesso!!");
			//Se foi criado com sucesso, retorno o objeto criado
			return new ResponseEntity<>(novo, HttpStatus.CREATED);
		} catch (Exception e) {
			LOGGER.error("Erro ao criar um contato", e);
			return ApiHttpStatus.internalServerError("Ocorreu algum erro na criação do contato");
		}
	}



}
