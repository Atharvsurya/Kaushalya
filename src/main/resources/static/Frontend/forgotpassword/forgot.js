document.querySelector('.btn').addEventListener('click', async () => {
    const email = document.querySelector('input[type="email"]').value;

    const response = await fetch('http://localhost:8080/api/auth/forgot-password', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email: email })
    });

    const data = await response.json();
    alert(data.message || "If email exists, reset link sent!");
});