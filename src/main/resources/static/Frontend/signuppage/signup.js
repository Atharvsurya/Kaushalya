document.querySelector('.btn').addEventListener('click', async (e) => {
    e.preventDefault();
    
    const fullName = document.querySelector('input[placeholder="Full Name"]').value;
    const email = document.querySelector('input[placeholder="Email"]').value;
    const password = document.getElementById("pass").value;
    const confirmPassword = document.getElementById("cpass").value;

    const response = await fetch('http://localhost:8080/api/auth/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ name: fullName, email: email, password: password, confirmPassword: confirmPassword })
    });

    const data = await response.json();
    if(data.success) {
        alert("Registration Successful!");
        window.location.href = "../loginpage/login.html";
    } else {
        alert(data.message);
    }
});