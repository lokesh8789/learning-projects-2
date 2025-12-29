package com.blog.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResouceNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	String resouceName;
	String fieldName;
	long fieldValue;
	String stringFieldValue;
	public ResouceNotFoundException(String resouceName, String fieldName, long fieldValue) {
		super(String.format("%s not found with %s : %s",resouceName,fieldName,fieldValue));
		this.resouceName = resouceName;
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}
	public ResouceNotFoundException(String resouceName, String fieldName, String stringFieldValue) {
		super(String.format("%s not found with %s : %s",resouceName,fieldName,stringFieldValue));
		this.resouceName = resouceName;
		this.fieldName = fieldName;
		this.stringFieldValue = stringFieldValue;
	}
	
}
