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

function contactarVendedor(idProd) {
    alert("¡Gracias por tu interés! Redirigiendo al contacto del vendedor...");
}
