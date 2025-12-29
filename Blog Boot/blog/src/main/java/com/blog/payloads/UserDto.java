package com.blog.payloads;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserDto {
	private int id;
	
	@NotEmpty
	@Size(min = 3,message = "Username must be greater than 2 characters")
	private String name;
	
	@NotEmpty
	@Email(message = "Email format not valid")
	private String email;
	
	@NotEmpty
	@Size(min=3,max = 10,message = "password must be within 3 to 10 letters")
	private String password;
	
	@NotEmpty
	private String about;
	
	private Set<RoleDto> roles=new HashSet<>();
}
