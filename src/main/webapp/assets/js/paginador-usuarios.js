document.addEventListener("DOMContentLoaded", function () {
    const itemsPerPage = 5;
    let currentPage = 1;

    const userCards = Array.from(document.querySelectorAll(".user-item-card"));
    const paginacionContainer = document.getElementById("paginacionContainer");
    const inputFiltro = document.getElementById("filtroUsuario");

    let usuariosVisibles = [...userCards];

    function renderPage(page) {
        currentPage = page;
        const totalPages = Math.ceil(usuariosVisibles.length / itemsPerPage);

        // Ocultar todos los usuarios
        userCards.forEach(card => card.style.setProperty('display', 'none', 'important'));

        // Mostrar solo los de la página actual
        const start = (page - 1) * itemsPerPage;
        const end = start + itemsPerPage;

        usuariosVisibles.slice(start, end).forEach(card => {
            card.style.setProperty('display', 'flex', 'important');
        });

        renderPagination(totalPages);
    }

    function renderPagination(totalPages) {
        paginacionContainer.innerHTML = "";

        if (totalPages <= 1) return;

        // Botón Anterior
        const prevLi = document.createElement("li");
        prevLi.className = `page-item ${currentPage === 1 ? 'disabled' : ''}`;
        prevLi.innerHTML = `<a class="page-link" href="#">&laquo;</a>`;
        prevLi.addEventListener("click", (e) => {
            e.preventDefault();
            if (currentPage > 1) renderPage(currentPage - 1);
        });
        paginacionContainer.appendChild(prevLi);

        // Botones de Páginas
        for (let i = 1; i <= totalPages; i++) {
            const li = document.createElement("li");
            li.className = `page-item ${i === currentPage ? 'active' : ''}`;
            li.innerHTML = `<a class="page-link" href="#">${i}</a>`;
            li.addEventListener("click", (e) => {
                e.preventDefault();
                renderPage(i);
            });
            paginacionContainer.appendChild(li);
        }

        // Botón Siguiente
        const nextLi = document.createElement("li");
        nextLi.className = `page-item ${currentPage === totalPages ? 'disabled' : ''}`;
        nextLi.innerHTML = `<a class="page-link" href="#">&raquo;</a>`;
        nextLi.addEventListener("click", (e) => {
            e.preventDefault();
            if (currentPage < totalPages) renderPage(currentPage + 1);
        });
        paginacionContainer.appendChild(nextLi);
    }

    // Integración con el Filtro de Búsqueda
    if (inputFiltro) {
        inputFiltro.addEventListener("keyup", function () {
            const query = this.value.toLowerCase().trim();

            usuariosVisibles = userCards.filter(card => {
                const text = card.textContent.toLowerCase();
                return text.includes(query);
            });

            renderPage(1);
        });
    }

    // Inicializar primera página
    renderPage(1);
});