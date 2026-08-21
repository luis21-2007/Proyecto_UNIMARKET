document.getElementById('formRegistro').addEventListener('submit', function (e) {
    const btn = document.getElementById('btnSubmit');

    // Deshabilitar el botón un milisegundo después para permitir que el formulario complete el envíos
    setTimeout(() => {
        btn.disabled = true;
        btn.innerHTML = '<span class="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>Creando...';
    }, 10);
});