package com.darrenlai.bobabuddies.models;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
    
public class LoginUser {
    
    @NotEmpty(message="Email field is empty")
    @Email(message="Please enter a valid email!")
    private String email;
    
    @NotEmpty(message="Password field is empty")
    private String password;
    
    public LoginUser() {}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
    
    
}