function cambiarImagenPrincipal(nuevaUrl, elementoClick) {
    // Cambia el src de la foto grande
    document.getElementById('imgPrincipal').src = nuevaUrl;

    // Quita la clase activa de todas las miniaturas
    document.querySelectorAll('.thumb-img').forEach(thumb => {
        thumb.classList.remove('active-thumb');
    });

    // Agrega la clase activa a la foto seleccionada
    elementoClick.classList.add('active-thumb');
}

document.addEventListener("DOMContentLoaded", function() {
    const formOferta = document.getElementById('formEnviarOferta');
    const btnSubmit = document.getElementById('btnSubmitOferta');

    if (formOferta && btnSubmit) {
        formOferta.addEventListener('submit', function() {
            // Deshabilitar botón y cambiar el estado visual al enviar el formulario
            btnSubmit.disabled = true;
            btnSubmit.innerHTML = '<i class="bi bi-hourglass-split me-1"></i> Enviando...';
        });
    }
});

