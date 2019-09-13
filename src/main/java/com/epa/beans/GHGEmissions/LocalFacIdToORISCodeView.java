package com.epa.beans.GHGEmissions;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

@Entity
@Table(name="localFacId_ORIS_view")
@Immutable
public class LocalFacIdToORISCodeView {
//	
//	@EmbeddedId
//	FacId_ORIS facId_ORISKey;
	
	@Id
	@Column(name="localFacId")
	String localFacId;
	
	@Column(name="ORISCode")
	String ORISCode;
	
	
}
//@Embeddable
//class FacId_ORIS implements Serializable {
//
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = -5236884226264528824L;
//
//
//	@Column(name="ORISCode")
//	String ORISCode;
//}