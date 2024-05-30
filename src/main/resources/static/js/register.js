document.getElementById('registerForm').addEventListener('submit', function (e) {
    e.preventDefault();

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    if (username.length < 5) {
        alert('Username must be at least 5 characters long.');
        e.preventDefault();
        return;
    }

    if (password.length < 8) {
        alert('Password must be at least 8 characters long.');
        e.preventDefault();
        return;
    }

    fetch('/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({username, password})
    })
        .then(response => {
            if (response.ok) {
                alert('Registration successful!');
                window.location.href = 'login.html';
            } else {
                alert('Registration failed.');
            }
        })
        .catch(error => console.error('Error:', error));
});