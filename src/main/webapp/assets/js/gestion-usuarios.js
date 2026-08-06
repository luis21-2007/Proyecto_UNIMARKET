function prepararModalEstado(idUsuario, nombreUsuario, accion) {
    // 1. Inyectar el nombre del usuario
    document.getElementById('usuarioNombreConfirm').textContent = nombreUsuario;

    // 2. Cambiar la palabra del modal según la acción ("deshabilitar" o "habilitar")
    const textoAccion = (accion === 'desactivar') ? 'deshabilitar' : 'habilitar';
    document.getElementById('accionTextoConfirm').textContent = textoAccion;

    // 3. Modificar la URL hacia el Servlet con la acción correspondiente
    document.getElementById('btnConfirmActionUsuario').href = 'gestionUsuarios?accion=' + accion + '&id=' + idUsuario;

    // 4. Mostrar el modal de Bootstrap
    var myModal = new bootstrap.Modal(document.getElementById('confirmModalUsuario'));
    myModal.show();
}
document.addEventListener('DOMContentLoaded', function() {
    const inputFiltro = document.getElementById('filtroUsuario');

    if (inputFiltro) {
        inputFiltro.addEventListener('keyup', function() {
            // Convertimos el texto ingresado a minúsculas
            const textoBuscado = this.value.toLowerCase().trim();

            // Seleccionamos todas las tarjetas de usuario
            const tarjetasUsuarios = document.querySelectorAll('.user-item-card');

            tarjetasUsuarios.forEach(function(tarjeta) {
                // Obtenemos el texto completo dentro de la tarjeta (Nombre, Apellido, Correo)
                const textoTarjeta = tarjeta.textContent.toLowerCase();

                // Si el texto de la tarjeta incluye lo buscado, la mostramos; si no, la ocultamos
                if (textoTarjeta.includes(textoBuscado)) {
                    tarjeta.classList.remove('d-none');
                    tarjeta.classList.add('d-flex');
                } else {
                    tarjeta.classList.remove('d-flex');
                    tarjeta.classList.add('d-none');
                }
            });
        });
    }
});