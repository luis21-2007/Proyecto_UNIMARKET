document.addEventListener("DOMContentLoaded", function () {
    const itemsPerPage = 5;
    let currentPage = 1;

    const categoryCards = Array.from(document.querySelectorAll(".categoria-item-card"));
    const paginacionContainer = document.getElementById("paginacionContainerCat");
    const inputFiltro = document.getElementById("filtroCategoria");

    let categoriasVisibles = [...categoryCards];

    function renderPage(page) {
        currentPage = page;
        const totalPages = Math.ceil(categoriasVisibles.length / itemsPerPage);

        categoryCards.forEach(card => card.style.setProperty('display', 'none', 'important'));

        const start = (page - 1) * itemsPerPage;
        const end = start + itemsPerPage;

        categoriasVisibles.slice(start, end).forEach(card => {
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

        // Números de Página
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

    if (inputFiltro) {
        inputFiltro.addEventListener("keyup", function () {
            const query = this.value.toLowerCase().trim();

            categoriasVisibles = categoryCards.filter(card => {
                const nombre = card.getAttribute("data-nombre") || "";
                return nombre.includes(query);
            });

            renderPage(1);
        });
    }

    renderPage(1);
});