package com.epa.beans.EIAGeneration;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

/**
 * This class is mapped to a view that holds 
 * generation value per registryId per year
 *
 */
@Entity
@Table(name="genPerRegPerYear")
@Immutable
public class GenerationPerRegistryIdView {
	
	@EmbeddedId
	GenViewKey genViewKey;
	
	@Column(name = "genSum")
	String genSum;
	
}

/**
 * The composite key for the table or view consists 
 * of the following elements
 *
 */
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
