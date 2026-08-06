document.addEventListener('DOMContentLoaded', function () {
    const inputBuscar = document.getElementById('inputBuscarProducto');
    const noResultados = document.getElementById('sinResultadosBusqueda');

    if (inputBuscar) {
        inputBuscar.addEventListener('input', function () {
            const textoBusqueda = this.value.toLowerCase().trim();
            const tarjetas = document.querySelectorAll('.product-card');
            let visibles = 0;

            tarjetas.forEach(function (tarjeta) {
                // Obtener el contenedor '.col' padre de la tarjeta
                const colPadre = tarjeta.closest('.col');
                const titulo = tarjeta.querySelector('.product-title');

                if (titulo) {
                    const nombreProducto = titulo.textContent.toLowerCase();

                    // Si el nombre contiene el texto buscado, mostrar; si no, ocultar
                    if (nombreProducto.includes(textoBusqueda)) {
                        if (colPadre) colPadre.style.display = '';
                        visibles++;
                    } else {
                        if (colPadre) colPadre.style.display = 'none';
                    }
                }
            });

            // Si hay productos en la base de datos pero ninguno coincide con la búsqueda
            if (noResultados) {
                if (visibles === 0 && tarjetas.length > 0) {
                    noResultados.classList.remove('d-none');
                } else {
                    noResultados.classList.add('d-none');
                }
            }
        });
    }
});
function filtrarPorCategoria(idCategoria) {
    document.getElementById('inputCategoriaId').value = idCategoria;
    document.getElementById('formFiltroCategoria').submit();
}