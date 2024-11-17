document.addEventListener('DOMContentLoaded', function() {
    const passwordInput = document.getElementById('password');
    const hideEye = document.getElementById('hideEye');
    const showEye = document.getElementById('showEye');

    hideEye.addEventListener('click', function() {
        passwordInput.type = 'text';
        hideEye.classList.add('hide');
        showEye.classList.remove('hide');
    });

    showEye.addEventListener('click', function() {
        passwordInput.type = 'password';
        showEye.classList.add('hide');
        hideEye.classList.remove('hide');
    });
});