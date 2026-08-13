function previewImages(event) {
    const container = document.getElementById('preview-container');
    const fileCountText = document.getElementById('file-count');
    container.innerHTML = ''; // Limpiar previsualizaciones anteriores

    const files = Array.from(event.target.files);

    if (files.length > 3) {
        alert("Solo puedes seleccionar un máximo de 3 imágenes.");
        event.target.value = ""; // Limpiar selección
        fileCountText.textContent = "";
        return;
    }

    if (files.length > 0) {
        fileCountText.textContent = `Has seleccionado ${files.length} imagen(es)`;
    } else {
        fileCountText.textContent = "";
    }

    files.forEach(file => {
        const reader = new FileReader();
        reader.onload = function(e) {
            const img = document.createElement('img');
            img.src = e.target.result;
            img.style.width = '70px';
            img.style.height = '70px';
            img.style.objectFit = 'cover';
            img.style.borderRadius = '8px';
            img.style.border = '1.5px solid #F0A515';
            container.appendChild(img);
        }
        reader.readAsDataURL(file);
    });
}
document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('formSubirProducto');
    const btnSubir = document.getElementById('btnSubirProducto');
    let enviando = false;

    if (form && btnSubir) {
        form.addEventListener('submit', function (e) {
            // Verificar si los campos requeridos son válidos
            if (!form.checkValidity()) {
                return; // Si el formulario no es válido, permite que el navegador muestre los errores
            }

            // Si ya se inició el envío, prevenimos cualquier submit adicional
            if (enviando) {
                e.preventDefault();
                return false;
            }

            // Marcar como enviado inmediatamente para bloquear clics instantáneos
            enviando = true;

            // Aplicar puntero inactivo e indicador en el botón de forma inmediata
            btnSubir.style.pointerEvents = 'none';
            btnSubir.style.opacity = '0.7';
            btnSubir.innerHTML = '<span class="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>Subiendo...';

            // Deshabilitar formalmente en el ciclo siguiente para asegurar que la petición HTTP/Multipart ya zarpó
            setTimeout(() => {
                btnSubir.disabled = true;
            }, 0);
        });
    }
});