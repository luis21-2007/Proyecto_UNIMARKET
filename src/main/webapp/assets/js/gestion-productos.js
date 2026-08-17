function prepararModalEstadoProducto(idProducto, nombreProducto, accion) {
    const textoAccion = document.getElementById('accionTextoConfirmProd');
    const textoNombre = document.getElementById('productoNombreConfirm');
    const btnConfirmar = document.getElementById('btnConfirmActionProducto');

    if (textoAccion) {
        textoAccion.textContent = accion === 'activar' ? 'activar' : 'deshabilitar';
    }

    if (textoNombre) {
        textoNombre.textContent = nombreProducto;
    }

    if (btnConfirmar) {
        // Redirige al Servlet encargado del cambio de estado
        btnConfirmar.href = `cambiarEstadoProducto?id=${idProducto}&accion=${accion}`;
    }

    const modalEl = document.getElementById('confirmModalProducto');
    if (modalEl && typeof bootstrap !== 'undefined') {
        const modal = bootstrap.Modal.getOrCreateInstance(modalEl);
        modal.show();
    }
}

document.addEventListener("DOMContentLoaded", function () {
    // Filtro en tiempo real por nombre de producto o vendedor
    const filtroInput = document.getElementById('filtroProducto');
    if (filtroInput) {
        filtroInput.addEventListener('input', function () {
            const query = this.value.toLowerCase().trim();
            const tarjetas = document.querySelectorAll('.user-item-card');

            tarjetas.forEach(tarjeta => {
                const nombre = tarjeta.getAttribute('data-nombre') || '';
                const vendedor = tarjeta.getAttribute('data-vendedor') || '';

                if (nombre.includes(query) || vendedor.includes(query)) {
                    tarjeta.style.setProperty('display', 'flex', 'important');
                } else {
                    tarjeta.style.setProperty('display', 'none', 'important');
                }
            });
        });
    }
});