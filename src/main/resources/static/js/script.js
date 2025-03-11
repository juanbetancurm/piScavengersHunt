document.addEventListener('DOMContentLoaded', function() {
    // Form validation for registration
    const registrationForm = document.querySelector('form[action="/register"]');
    if (registrationForm) {
        registrationForm.addEventListener('submit', function(e) {
            const teamName = document.getElementById('teamName').value.trim();
            const gradeClass = document.getElementById('gradeClass').value.trim();
            const memberNames = document.getElementById('memberNames').value.trim();

            if (!teamName || !gradeClass || !memberNames) {
                e.preventDefault();
                alert('Robledista, please fill in all required fields.');
            }
        });
    }

    // Timer functionality (updates every second)
    let startTime;
    const timerElement = document.getElementById('timer');

    if (timerElement) {
        startTime = new Date();

        setInterval(function() {
            const currentTime = new Date();
            const elapsedTime = Math.floor((currentTime - startTime) / 1000);

            const minutes = Math.floor(elapsedTime / 60);
            const seconds = elapsedTime % 60;

            timerElement.textContent = `${minutes}:${seconds.toString().padStart(2, '0')}`;
        }, 1000);
    }

    // Code input validation
    const codeForm = document.querySelector('form[action="/check-code"]');
    if (codeForm) {
        codeForm.addEventListener('submit', function(e) {
            const code = document.getElementById('code').value.trim();

            if (!code) {
                e.preventDefault();
                alert('Please enter the code.');
            }
        });
    }
});