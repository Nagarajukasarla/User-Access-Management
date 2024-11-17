document.addEventListener("DOMContentLoaded", () => {
    const requestForms = document.querySelectorAll(".request-access-form");

    requestForms.forEach((form) => {
        form.addEventListener("submit", (event) => {
            const reasonInput = form.querySelector("input[name='reason']");
            if (!reasonInput.value.trim()) {
                event.preventDefault();
                alert("Please enter a reason for requesting access.");
            }
        });
    });
});
