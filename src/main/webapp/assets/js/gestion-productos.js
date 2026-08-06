document.addEventListener('DOMContentLoaded', function () {
    // Filtro de búsqueda en tiempo real
    const filtroInput = document.getElementById('filtroProducto');
    if (filtroInput) {
        filtroInput.addEventListener('keyup', function () {
            const valor = this.value.toLowerCase().trim();
            const items = document.querySelectorAll('.user-item-card');

            items.forEach(function (item) {
                const texto = item.textContent.toLowerCase();
                if (texto.includes(valor)) {
                    item.classList.remove('d-none');
                    item.classList.add('d-flex');
                } else {
                    item.classList.remove('d-flex');
                    item.classList.add('d-none');
                }
            });
        });
    }
});

// Función global que activa el modal de Bootstrap
function prepararModalEstadoProducto(idProducto, nombreProducto, accion) {
    const spanAccion = document.getElementById('accionTextoConfirmProd');
    const strongNombre = document.getElementById('productoNombreConfirm');
    const btnConfirmar = document.getElementById('btnConfirmActionProducto');

    // Asignar los textos dinámicos
    strongNombre.textContent = nombreProducto;

    if (accion === 'desactivar') {
        spanAccion.textContent = 'deshabilitar';
        btnConfirmar.href = 'gestionProductos?id=' + idProducto + '&accion=desactivar';
        btnConfirmar.className = 'btn btn-confirmar btn-danger';
    } else {
        spanAccion.textContent = 'activar';
        btnConfirmar.href = 'gestionProductos?id=' + idProducto + '&accion=activar';
        btnConfirmar.className = 'btn btn-confirmar btn-success';
    }

    // Obtener e instanciar el Modal con Bootstrap 5
    const modalElement = document.getElementById('confirmModalProducto');
    if (modalElement) {
        const modal = bootstrap.Modal.getOrCreateInstance(modalElement);
        modal.show();
    }
}