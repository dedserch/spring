function showError(elementId, message) {
    const element = document.getElementById(elementId);
    element.classList.add('is-invalid');
    const errorElement = document.getElementById(`${elementId}Error`);
    errorElement.textContent = message;
    errorElement.style.display = 'block';
}

function clearErrors() {
    document.querySelectorAll('.is-invalid').forEach(el => el.classList.remove('is-invalid'));
    document.querySelectorAll('.invalid-feedback').forEach(el => el.style.display = 'none');
}

function validateRegister() {
    clearErrors();
    let valid = true;

    const username = document.getElementById('username').value.trim();
    const email = document.getElementById('email').value.trim();
    const password = document.getElementById('password').value.trim();

    if (username.length < 3 || username.length > 20) {
        showError('username', 'Username must be 3-20 characters');
        valid = false;
    }

    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
        showError('email', 'Invalid email format');
        valid = false;
    }

    if (password.length < 6) {
        showError('password', 'Password must be ≥6 characters');
        valid = false;
    }

    return valid;
}

function validateLogin() {
    clearErrors();
    let valid = true;

    const username = document.getElementById('loginUsername').value.trim();
    const password = document.getElementById('loginPassword').value.trim();

    if (!username) {
        showError('loginUsername', 'Username required');
        valid = false;
    }

    if (!password) {
        showError('loginPassword', 'Password required');
        valid = false;
    }

    return valid;
}