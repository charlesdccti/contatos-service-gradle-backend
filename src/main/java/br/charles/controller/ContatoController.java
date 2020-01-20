package br.charles.controller;


import java.util.Optional;

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
import br.charles.model.Contato;
import br.charles.repository.ContatoRepository;
import br.charles.util.ApiError;
import io.swagger.annotations.ApiOperation;



@RestController
@RequestMapping("/")
public class ContatoController {

	@Autowired
	ContatoRepository contatoRepository;

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());




	@GetMapping("/{idContato}")
	public ResponseEntity<Contato> getContato(@PathVariable(value = "idContato") String idContato) {

		String id = ""+idContato;
		Optional<Contato> rastreador = contatoRepository.findById(id);

		if (!rastreador.isPresent()) {
			return ApiError.notFound("Contato não encontrado");
		}
		return new ResponseEntity<>(rastreador.get(), HttpStatus.OK);
	}



	@PutMapping("/{idContato}")
	public ResponseEntity updateContato(@PathVariable String idContato, @RequestBody Contato dto) {

		try {
			Optional<Contato> contatoAtual = contatoRepository.findById(idContato);

			if (!contatoAtual.isPresent()) {
				return ApiError.notFound("Contato não encontrado");
			}

			if (dto.getNome() != null) {
				if (dto.getNome().length() < 2) {
					return ApiError.badRequest("Nome do produto inválido");
				}
				contatoAtual.get().setNome(dto.getNome());
			}

			if (dto.getValor() != null) {
				int valor = Integer.parseInt(dto.getValor());

				if (valor <= 0) {
					return ApiError.badRequest("Valor do produto inválido");
				}
				contatoAtual.get().setValor(dto.getValor());
			}

			Contato atualizado = contatoRepository.save(contatoAtual.get());

			if (atualizado == null) {
				return ApiError.internalServerError("Erro na atualização do produto");
			}

			LOGGER.info("Contato foi atualizado com sucesso!!");
			return new ResponseEntity<>(atualizado, HttpStatus.CREATED);

		} catch (Exception e) {
			LOGGER.error("Erro ao atualizar um Contato", e);
			return ApiError.internalServerError("Erro na atualização do contato");
		}
	}





	@DeleteMapping("/{idContato}")	@ApiOperation(value = "Apaga um objeto do tipo Contato")
	public ResponseEntity deleteContato(@PathVariable String idContato) {

		Optional<Contato> contato = contatoRepository.findById(idContato);

		if (!contato.isPresent()) {
			return ApiError.notFound("Contato não encontrado");
		} else {
			contatoRepository.deleteById(idContato);
		}

		LOGGER.info("Contato foi removido com sucesso!!");
		return new ResponseEntity<>(HttpStatus.OK);
	}




	@ApiOperation(value = "Lista os Contatos")
	@GetMapping("/")
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



	@ApiOperation(value = "Cria um novo Contato")
	@PostMapping("/")
	public ResponseEntity createContato(@RequestBody Contato dto) {

		try {
			Contato contato = new Contato();

			if (dto.getNome() == null || dto.getNome().length() < 2) {
				return ApiError.badRequest("Informe o nome do contato");
			}

			if (dto.getCanal() == null || dto.getCanal().length() < 2) {
				return ApiError.badRequest("Informe o canal do contato");
			}

			Integer valor = Integer.parseInt(dto.getValor());

			if (valor == null || valor <= 0) {
				return ApiError.badRequest("Valor do contato inválido");
			}

			if (dto.getObs() == null || dto.getObs().length() < 2) {
				return ApiError.badRequest("Informe a observação do contato");
			}
			contato.setIdContato(null);
			contato.setNome(dto.getNome());
			contato.setCanal(dto.getCanal());
			contato.setValor(dto.getValor());
			contato.setObs(dto.getObs());

			Contato novo = contatoRepository.save(contato);

			//Se ocorreu algum erro, retorno esse erro para a API
			if (novo == null) {
				return ApiError.badRequest("Ocorreu algum erro na criação do contato");
			}

			LOGGER.info("Contato foi criado com sucesso!!");
			//Se foi criado com sucesso, retorno o objeto criado
			return new ResponseEntity<>(novo, HttpStatus.CREATED);
		} catch (Exception e) {
			LOGGER.error("Erro ao criar um contato", e);
			return ApiError.internalServerError("Ocorreu algum erro na criação do contato");
		}
	}



}
