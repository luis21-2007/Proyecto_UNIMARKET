document.addEventListener("DOMContentLoaded", function () {
    const canvas = document.getElementById('productosChart');
    if (!canvas) return;

    // Leemos los datos enviados desde el JSP
    const totalProductos = parseInt(canvas.getAttribute('data-productos')) || 0;
    const totalUsuarios = parseInt(canvas.getAttribute('data-usuarios')) || 0;

    const ctx = canvas.getContext('2d');

    new Chart(ctx, {
        type: 'bar',
        data: {
            labels: ['Total Productos', 'Total Usuarios'],
            datasets: [{
                label: 'Registros en Plataforma',
                data: [totalProductos, totalUsuarios],
                backgroundColor: [
                    'rgba(139, 0, 0, 0.7)',  // Guinda
                    'rgba(243, 156, 18, 0.7)' // Naranja
                ],
                borderColor: [
                    '#8B0000',
                    '#f39c12'
                ],
                borderWidth: 2,
                borderRadius: 8
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { display: false }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: { stepSize: 1 }
                }
            }
        }
    });
});