document.getElementById("loginBtn").addEventListener("click", function(event) {
    event.preventDefault();
    login();
});
async function login(event){

     if (event) event.preventDefault();
    console.log("Login fun started")

    let email = document.getElementById("email").value;
    let password = document.getElementById("password").value;

    if (!email || !password) {
            alert("Please fill in all fields");
            return;
        }

    try{
        console.log("2. Sending fetch request...");
        const response = await fetch('http://localhost:8080/api/userlogin',
           { method : 'POST',
            headers : { 'Content-Type': 'application/json' },
            body : JSON.stringify({ email: email, pass: password })
            }
        );

        console.log("3. Response received, status:", response.status);
        const result = await response.json();
        if (response.ok) {
            alert("Login Successful");
            localStorage.setItem("userFName", result.name.split(" ")[0]);
            window.location.href = "../Dashboard/home.html";
        } else {
            alert("Invalid");
        }   
    }

    catch (error) {
        console.error("Error:", error);
        alert("Could not connect to the server.");
    }
    return false;
}