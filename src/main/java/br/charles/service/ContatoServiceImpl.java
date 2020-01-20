package br.charles.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.charles.repository.ContatoRepository;

@Component
public class ContatoServiceImpl {

	@Autowired
	private ContatoRepository contatoRepository;


}
