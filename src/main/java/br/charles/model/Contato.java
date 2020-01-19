package br.charles.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/**
 * @author charles
 *
 */
@Entity
@Data
public class Contato {

	@Id 
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	private String idContato;

	@NotNull
	private String nome;
	
	@NotNull
	private String canal;
	
	@NotNull
	private String valor;
	
	private String obs;

}