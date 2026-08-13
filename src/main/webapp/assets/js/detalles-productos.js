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

document.addEventListener("DOMContentLoaded", function() {

    // ==========================================
    // 1. FORMULARIO DE OFERTA (BLINDADO CONTRA DUPLICADOS)
    // ==========================================
    const formOferta = document.getElementById('formEnviarOferta');
    const btnSubmitOferta = document.getElementById('btnSubmitOferta');
    const btnConfirmarOfertaModal = document.getElementById('btnConfirmarEnviarOferta');

    let enviandoOferta = false;

    function procesarEnvioOferta(e) {
        // 1. Verificar si el formulario cumple validaciones HTML5 (monto válido, campos requeridos)
        if (formOferta && !formOferta.checkValidity()) {
            return false;
        }

        // 2. Si ya se está enviando, prevenir peticiones dobles
        if (enviandoOferta) {
            if (e) e.preventDefault();
            return false;
        }

        // 3. Activar bandera de envío
        enviandoOferta = true;

        // 4. Deshabilitar botón de submit estándar
        if (btnSubmitOferta) {
            btnSubmitOferta.style.pointerEvents = 'none';
            btnSubmitOferta.innerHTML = '<i class="bi bi-hourglass-split me-1"></i> Enviando...';
            setTimeout(() => { btnSubmitOferta.disabled = true; }, 0);
        }

        // 5. Deshabilitar botón del modal en caso de existir
        if (btnConfirmarOfertaModal) {
            btnConfirmarOfertaModal.style.pointerEvents = 'none';
            btnConfirmarOfertaModal.innerHTML = '<i class="bi bi-hourglass-split me-1"></i> Enviando...';
            setTimeout(() => { btnConfirmarOfertaModal.disabled = true; }, 0);
        }
    }

    // Intercepta el evento submit nativo (clic en tipo submit o pulsar ENTER)
    if (formOferta) {
        formOferta.addEventListener('submit', function (e) {
            if (!formOferta.checkValidity()) {
                return; // Deja que el navegador muestre las alertas nativas si faltan datos
            }

            if (enviandoOferta) {
                e.preventDefault();
                return false;
            }

            procesarEnvioOferta(e);
        });
    }

    // Intercepta clic directo si usas un botón manual en un modal
    if (btnConfirmarOfertaModal && formOferta) {
        btnConfirmarOfertaModal.addEventListener('click', function (e) {
            if (enviandoOferta) {
                e.preventDefault();
                return false;
            }

            if (!formOferta.checkValidity()) {
                formOferta.reportValidity(); // Muestra mensajes de validación HTML5
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
    // 2. FORMULARIO DE EDICIÓN DE PRODUCTO CON MODAL Y ENTER CONTROLADO
    // ==========================================
    const formEditar = document.getElementById('formEditarProducto');
    const btnConfirm = document.getElementById('btnConfirmActionProducto');
    const btnGuardar = formEditar ? formEditar.querySelector('.btn-guardar-edit') : null;

    if (formEditar) {
        let formConfirmado = false;

        // Intercepta la tecla ENTER o cualquier submit directo
        formEditar.addEventListener('submit', function(e) {
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
            btnConfirm.addEventListener('click', function() {
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
            const initialData = new FormData(formEditar);

            const checkChanges = () => {
                let hasChanged = false;

                for (let [key, value] of initialData.entries()) {
                    const input = formEditar.elements[key];
                    if (input && input.type !== 'file') {
                        if (input.value !== value) {
                            hasChanged = true;
                            break;
                        }
                    }
                }

                const fileInput = formEditar.querySelector('input[type="file"]');
                if (fileInput && fileInput.files.length > 0) {
                    hasChanged = true;
                }

                btnGuardar.disabled = !hasChanged;
            };

            formEditar.addEventListener('input', checkChanges);
            formEditar.addEventListener('change', checkChanges);
        }
    }
});