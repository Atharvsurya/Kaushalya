const profileBtn = document.getElementById("profileBtn");
const profileMenu = document.getElementById("profileMenu");

profileBtn.addEventListener("click", () => {

profileMenu.style.display =
profileMenu.style.display === "block" ? "none" : "block";

});

window.addEventListener("click", function(e){

if(!profileBtn.contains(e.target) && !profileMenu.contains(e.target)){

profileMenu.style.display = "none";

}

});