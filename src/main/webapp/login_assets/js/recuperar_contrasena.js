document.addEventListener("DOMContentLoaded", function() {
    const recoverForm = document.getElementById("recover-form");
    const overlay = document.getElementById("overlay");
    const notificationModal = document.getElementById("notificationModal");
    const acceptBtn = document.getElementById("acceptBtn");

    if (recoverForm) {
        recoverForm.addEventListener("submit", function(event) {

            // Primero enviar el formulario
            recoverForm.submit();

            // Mostrar el modal después de un pequeño retraso para asegurar que el formulario se envíe
            setTimeout(function() {
                overlay.style.display = "block";
                notificationModal.style.display = "block";
            }, 500); // Ajusta el tiempo si es necesario
        });

        acceptBtn.addEventListener("click", function(event) {
            event.preventDefault(); // Previene la redirección del enlace

            // Cerrar el modal
            overlay.style.display = "none";
            notificationModal.style.display = "none";
        });
    }
});