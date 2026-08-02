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