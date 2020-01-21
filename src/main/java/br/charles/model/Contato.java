package br.charles.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/**
 * @author charles
 *
 */
@Entity
@Data
public class Contato {

//	@GeneratedValue
//	@GenericGenerator(name="system-uuid", strategy = "uuid")
	@Id 
	private String idContato;

	@NotNull(message = "O campo 'nome' não pode ser nulo!")
	@NotEmpty(message = "O campo 'nome' não pode ser uma string vazia!")
	@Size(min=2, message="O campo 'nome' não pode ser menor que 2 characters")
	private String nome;
	
	@NotNull(message = "O campo 'canal' não pode ser nulo!")
	@NotEmpty(message = "O campo 'canal' não pode ser uma string vazia!")
	private String canal;
	
	@NotNull(message = "O campo 'valor' não pode ser nulo!")
	@NotEmpty(message = "O campo 'valor' não pode ser uma string vazia!")
	private String valor;
	
	private String obs;

}