 document.getElementById('categoriaForm').addEventListener('submit', function (e) {
        e.preventDefault();

        const btn = document.getElementById('btnSubir');
        const alertaContainer = document.getElementById('alertaContainer');
        const inputNombre = document.getElementById('nombreCategoria');

        btn.disabled = true;
        btn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Guardando...';

        const params = new URLSearchParams();
        params.append('action', 'create');
        params.append('nombreCategoria', inputNombre.value);

        fetch('categorias', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
            },
            body: params
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    // Éxito: Redirigir a la vista principal
                    window.location.href = 'categorias?msg=creada';
                } else {
                    // Mostrar error retornado por la respuesta JSON del servlet
                    alertaContainer.innerHTML = `
                <div class="alert alert-danger py-2 px-3 mb-3 text-start" style="font-size: 15px; border-radius: 8px;">
                    <i class="bi bi-exclamation-triangle-fill me-2"></i> ${data.message}
                </div>
            `;
                    btn.disabled = false;
                    btn.innerHTML = 'Subir Categoría';
                }
            })
            .catch(err => {
                console.error(err);
                alertaContainer.innerHTML = `
            <div class="alert alert-danger py-2 px-3 mb-3 text-start" style="font-size: 15px; border-radius: 8px;">
                <i class="bi bi-exclamation-triangle-fill me-2"></i> Error al conectar con el servidor.
            </div>
        `;
                btn.disabled = false;
                btn.innerHTML = 'Subir Categoría';
            });
    });