document.addEventListener('DOMContentLoaded', function () {
    // Modal de Edición
    const editModal = document.getElementById('editCategoriaModal');
    if (editModal) {
        editModal.addEventListener('show.bs.modal', function (event) {
            const button = event.relatedTarget;
            const id = button.getAttribute('data-id');
            const nombre = button.getAttribute('data-nombre');

            document.getElementById('editCategoriaId').value = id;
            document.getElementById('editNombreCategoria').value = nombre;
        });
    }

    // Modal de Confirmación / Eliminación
    const confirmModal = document.getElementById('confirmModal');
    if (confirmModal) {
        confirmModal.addEventListener('show.bs.modal', function (event) {
            const button = event.relatedTarget;
            const id = button.getAttribute('data-id');
            const nombre = button.getAttribute('data-nombre');

            document.getElementById('categoriaNombreConfirm').textContent = nombre;
            document.getElementById('btnConfirmAction').href = 'categorias?action=delete&id=' + id;
        });
    }
});