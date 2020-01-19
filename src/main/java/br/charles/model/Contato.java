package br.charles.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * @author charles
 *
 */
@Entity
@Data
public class Contato {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private String idContato;

	@NotNull
	private String nome;
	
	@NotNull
	private String canal;
	
	@NotNull
	private String valor;
	
	private String obs;

}