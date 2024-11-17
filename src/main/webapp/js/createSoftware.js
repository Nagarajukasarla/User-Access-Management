document.addEventListener("DOMContentLoaded", () => {
    const createSoftwareContainer = document.querySelector(".create-software");
    const createButton = document.querySelector(".create-btn");
    const cancelButton = document.querySelector(".cancel-btn");

    createButton.addEventListener("click", () => {
        createSoftwareContainer.classList.remove("hide-create-container");
        createSoftwareContainer.classList.add("show-create-container");
    });

    cancelButton.addEventListener("click", () => {
        createSoftwareContainer.classList.remove("show-create-container");
        createSoftwareContainer.classList.add("hide-create-container");
    });

})