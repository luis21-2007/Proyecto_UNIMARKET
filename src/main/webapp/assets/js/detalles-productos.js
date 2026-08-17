function cambiarImagenPrincipal(nuevaUrl, elementoClick) {
    // Cambia el src de la foto grande
    const imgPrincipal = document.getElementById('imgPrincipal');
    if (imgPrincipal) {
        imgPrincipal.src = nuevaUrl;
    }

    // Quita la clase activa de todas las miniaturas
    document.querySelectorAll('.thumb-img').forEach(thumb => {
        thumb.classList.remove('active-thumb');
    });

    // Agrega la clase activa a la foto seleccionada
    if (elementoClick) {
        elementoClick.classList.add('active-thumb');
    }
}

function abrirModalEliminarImagen(url) {
    const btnConfirmar = document.getElementById('btnConfirmarEliminarImagen');
    if (btnConfirmar) {
        btnConfirmar.setAttribute('href', url);
    }
    const modalEl = document.getElementById('confirmEliminarImagenModal');
    if (modalEl && typeof bootstrap !== 'undefined') {
        const modal = bootstrap.Modal.getOrCreateInstance(modalEl);
        modal.show();
    }
}

document.addEventListener("DOMContentLoaded", function() {

    // ==========================================
    // DELEGACIÓN DE EVENTOS PARA EL BOTÓN X (ELIMINAR IMAGEN)
    // ==========================================
    document.addEventListener('click', function (e) {
        const btnX = e.target.closest('.btn-eliminar-imagen-trigger');
        if (btnX) {
            e.preventDefault();
            const targetUrl = btnX.getAttribute('data-url');
            if (targetUrl) {
                abrirModalEliminarImagen(targetUrl);
            }
        }
    });

    // ==========================================
    // 1. FORMULARIO DE OFERTA (BLINDADO CONTRA DUPLICADOS)
    // ==========================================
    const formOferta = document.getElementById('formEnviarOferta');
    const btnSubmitOferta = document.getElementById('btnSubmitOferta');
    const btnConfirmarOfertaModal = document.getElementById('btnConfirmarEnviarOferta');

    let enviandoOferta = false;

    function procesarEnvioOferta(e) {
        if (formOferta && !formOferta.checkValidity()) {
            return false;
        }

        if (enviandoOferta) {
            if (e) e.preventDefault();
            return false;
        }

        enviandoOferta = true;

        if (btnSubmitOferta) {
            btnSubmitOferta.style.pointerEvents = 'none';
            btnSubmitOferta.innerHTML = '<i class="bi bi-hourglass-split me-1"></i> Enviando...';
            setTimeout(() => {
                btnSubmitOferta.disabled = true;
            }, 0);
        }

        if (btnConfirmarOfertaModal) {
            btnConfirmarOfertaModal.style.pointerEvents = 'none';
            btnConfirmarOfertaModal.innerHTML = '<i class="bi bi-hourglass-split me-1"></i> Enviando...';
            setTimeout(() => {
                btnConfirmarOfertaModal.disabled = true;
            }, 0);
        }
    }

    if (formOferta) {
        formOferta.addEventListener('submit', function (e) {
            if (!formOferta.checkValidity()) {
                return;
            }

            if (enviandoOferta) {
                e.preventDefault();
                return false;
            }

            procesarEnvioOferta(e);
        });
    }

    if (btnConfirmarOfertaModal && formOferta) {
        btnConfirmarOfertaModal.addEventListener('click', function (e) {
            if (enviandoOferta) {
                e.preventDefault();
                return false;
            }

            if (!formOferta.checkValidity()) {
                formOferta.reportValidity();
                return;
            }

            procesarEnvioOferta(e);

            if (typeof formOferta.requestSubmit === 'function') {
                formOferta.requestSubmit();
            } else {
                formOferta.submit();
            }
        });
    }

    // ==========================================
    // 2. FORMULARIO DE EDICIÓN DE PRODUCTO CON MODAL Y DETECCIÓN DE CAMBIOS
    // ==========================================
    const formEditar = document.getElementById('formEditarProducto');
    const btnConfirm = document.getElementById('btnConfirmActionProducto');
    const btnGuardar = formEditar ? formEditar.querySelector('.btn-guardar-edit') : null;

    if (formEditar) {
        let formConfirmado = false;

        // Intercepta la tecla ENTER o cualquier submit directo
        formEditar.addEventListener('submit', function (e) {
            if (btnGuardar && btnGuardar.disabled) {
                e.preventDefault();
                return false;
            }

            if (!formConfirmado) {
                e.preventDefault(); // Detiene el envío nativo por Enter

                const modalEl = document.getElementById('confirmModalProducto');
                if (modalEl && typeof bootstrap !== 'undefined') {
                    const modal = bootstrap.Modal.getOrCreateInstance(modalEl);
                    modal.show();
                }
                return false;
            }
        });

        // Evento del botón Confirmar dentro del Modal
        if (btnConfirm) {
            btnConfirm.addEventListener('click', function () {
                if (formConfirmado) return;

                formConfirmado = true;
                this.disabled = true;
                this.style.pointerEvents = 'none';
                this.innerHTML = '<i class="bi bi-hourglass-split me-1"></i> Guardando...';

                formEditar.submit();
            });
        }

        // Detección de cambios para habilitar/deshabilitar botón guardar
        if (btnGuardar) {
            // Guardamos los valores iniciales de cada elemento de forma explícita
            const initialValues = {};
            Array.from(formEditar.elements).forEach(input => {
                if (input.name && input.type !== 'file') {
                    initialValues[input.name] = input.value;
                }
            });

            const checkChanges = () => {
                let hasChanged = false;

                // Comparar textos, números y selects
                for (let name in initialValues) {
                    const input = formEditar.elements[name];
                    if (input && input.value !== initialValues[name]) {
                        hasChanged = true;
                        break;
                    }
                }

                // Comparar si seleccionó imágenes nuevas
                const fileInput = formEditar.querySelector('input[type="file"]');
                if (fileInput && fileInput.files && fileInput.files.length > 0) {
                    hasChanged = true;
                }

                btnGuardar.disabled = !hasChanged;
            };

            formEditar.addEventListener('input', checkChanges);
            formEditar.addEventListener('change', checkChanges);
        }
    }
});