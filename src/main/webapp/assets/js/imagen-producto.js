let selectedFiles = [];

// Mostrar alerta visual en el contenedor Bootstrap
function mostrarAlerta(mensaje) {
    const alertaBox = document.getElementById('alertaJS');
    const mensajeBox = document.getElementById('mensajeAlertaJS');

    if (alertaBox && mensajeBox) {
        mensajeBox.textContent = mensaje;
        alertaBox.classList.remove('d-none');
        alertaBox.scrollIntoView({ behavior: 'smooth', block: 'center' });
    }
}

// Ocultar alerta visual
function ocultarAlerta() {
    const alertaBox = document.getElementById('alertaJS');
    if (alertaBox) {
        alertaBox.classList.add('d-none');
    }
}
function previewImages(event) {
    const input = event.target;
    const newFiles = Array.from(input.files);

    if (newFiles.length === 0) return;

    // Validar que la suma de las fotos anteriores + las nuevas no supere las 3 imágenes
    if (selectedFiles.length + newFiles.length > 3) {
        mostrarAlerta("Solo puedes subir un máximo de 3 imágenes en total.");
        input.value = ""; // Limpia la selección parcial
        return;
    }

    // Acumular los nuevos archivos en el arreglo global
    selectedFiles = selectedFiles.concat(newFiles);

    ocultarAlerta();
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
    selectedFiles.splice(indexToRemove, 1);
    renderPreviews();
}

// Lógica de validación del formulario
document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('formSubirProducto');
    const btnSubir = document.getElementById('btnSubirProducto');
    let enviando = false;

    if (form && btnSubir) {
        form.addEventListener('submit', function (e) {

            // 1. Validar si está vacío
            if (selectedFiles.length === 0) {
                e.preventDefault();
                mostrarAlerta("Hace falta adjuntar las imágenes del producto.");
                return false;
            }

            // 2. Validar que sean exactamente 3 imágenes
            if (selectedFiles.length !== 3) {
                e.preventDefault();
                mostrarAlerta("Es obligatorio subir exactamente 3 imágenes del producto.");
                return false;
            }

            // 3. Validar los inputs de texto HTML5
            if (!form.checkValidity()) {
                return;
            }

            // 4. Prevenir envíos dobles
            if (enviando) {
                e.preventDefault();
                return false;
            }

            ocultarAlerta();
            enviando = true;
            btnSubir.style.pointerEvents = 'none';
            btnSubir.style.opacity = '0.7';
            btnSubir.innerHTML = '<span class="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>Subiendo...';

            setTimeout(() => {
                btnSubir.disabled = true;
            }, 0);
        });
    }
});