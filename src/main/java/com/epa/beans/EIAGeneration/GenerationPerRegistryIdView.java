package com.epa.beans.EIAGeneration;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;


@Entity
@Table(name="genPerRegPerYear")
@Immutable
public class GenerationPerRegistryIdView {
	
	@EmbeddedId
	GenViewKey genViewKey;
	
	@Column(name = "genSum")
	String genSum;
	
}

@Embeddable
class GenViewKey implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1588120251417739694L;

	@Column(name = "registryId")
	String registryId;

	@Column(name = "genYear")
	String genYear;

}
