document.getElementById('loginForm').addEventListener('submit', function (e) {
    e.preventDefault();

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    fetch('/authenticate', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({username, password})
    })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Login failed.');
            }
        })
        .then(data => {
            localStorage.setItem('accessToken', data.accessToken);
            localStorage.setItem('refreshToken', data.refreshToken);
            alert('Login successful!');
            window.location.href = '/dashboard.html'; // Redirect to your staff page or dashboard
        })
        .catch(error => alert(error.message));
});