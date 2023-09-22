const leftButton = document.querySelector("#yourNavLeftButton");
const rightButton = document.querySelector("#yourNavRightButton");
const allEvents = document.querySelector("#allUserJoinedEvents");
const hostedEvents = document.querySelector("#hostedEvents");
	
function showLeft() {
	allEvents.style.display = "block";
	hostedEvents.style.display = "none";
}
	
function showRight() {
	allEvents.style.display = "none";
	hostedEvents.style.display = "block";
}