// Variable global para almacenar y manipular los archivos seleccionados
let selectedFiles = [];

function previewImages(event) {
    const input = event.target;
    const newFiles = Array.from(input.files);

    // Limitar selección a un máximo de 3 imágenes
    if (newFiles.length > 3) {
        alert("Solo puedes seleccionar un máximo de 3 imágenes.");
        input.value = "";
        selectedFiles = [];
        renderPreviews();
        return;
    }

    selectedFiles = newFiles;
    renderPreviews();
}

function renderPreviews() {
    const container = document.getElementById('preview-container');
    const fileCountText = document.getElementById('file-count');
    const input = document.getElementById('input-file');

    container.innerHTML = ''; // Limpiar previsualizaciones anteriores

    if (selectedFiles.length === 0) {
        fileCountText.textContent = "";
        input.value = "";
        return;
    }

    fileCountText.textContent = `Has seleccionado ${selectedFiles.length} imagen(es)`;

    // Sincronizar el input de archivos real mediante DataTransfer
    const dt = new DataTransfer();
    selectedFiles.forEach(file => dt.items.add(file));
    input.files = dt.files;

    // Generar vistas previas con botón "X"
    selectedFiles.forEach((file, index) => {
        const reader = new FileReader();
        reader.onload = function(e) {
            const wrapper = document.createElement('div');
            wrapper.className = 'position-relative d-inline-block';
            wrapper.style.margin = '4px';

            wrapper.innerHTML = `
                <img src="${e.target.result}" style="width: 70px; height: 70px; object-fit: cover; border-radius: 8px; border: 1.5px solid #F0A515;">
                <button type="button" 
                        onclick="removeSingleImage(${index})" 
                        class="btn btn-danger btn-sm rounded-circle p-0 d-flex align-items-center justify-content-center shadow-sm" 
                        style="position: absolute; top: -6px; right: -6px; width: 20px; height: 20px; font-size: 12px; font-weight: bold; line-height: 1; border: 1px solid #fff; background-color: #dc3545; color: #fff; cursor: pointer;">
                    &times;
                </button>
            `;

            container.appendChild(wrapper);
        };
        reader.readAsDataURL(file);
    });
}

function removeSingleImage(indexToRemove) {
    // Quitar del arreglo local y reconstruir vista/input
    selectedFiles.splice(indexToRemove, 1);
    renderPreviews();
}

// Lógica de validación y prevención de doble submit en el formulario
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