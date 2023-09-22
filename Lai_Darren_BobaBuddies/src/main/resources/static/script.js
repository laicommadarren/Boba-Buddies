
const loginForm = document.querySelector("#loginForm");
const registerForm = document.querySelector("#registerForm");
	
function showLogin() {
	loginForm.style.display = "block";
	registerForm.style.display = "none";
}
	
function showRegister() {
	loginForm.style.display = "none";
	registerForm.style.display = "block";
}