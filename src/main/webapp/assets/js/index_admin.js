const ctx = document.getElementById('productosChart').getContext('2d');
const productosChart = new Chart(ctx, {
    type: 'line',
    data: {
        labels: ['Lun', 'Mar', 'Mie', 'Jue', 'Vie', 'Sab', 'Dom'],
        datasets: [{
            label: 'Productos Subidos',
            data: [10, 50, 75, 50, 25, 75, 100],
            borderColor: '#c64646',
            backgroundColor: 'transparent',
            pointBackgroundColor: '#fff',
            pointBorderColor: '#c64646',
            pointBorderWidth: 2,
            pointRadius: 4,
            borderWidth: 2,
            tension: 0
        }]
    },
    options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
            legend: {
                display: false
            }
        },
        scales: {
            y: {
                min: 25,
                max: 125,
                ticks: {
                    stepSize: 25,
                    color: '#000',
                    font: { size: 14, weight: 'bold' }
                },
                grid: {
                    color: '#a0a0a0',
                    drawBorder: false,
                    borderDash: [10, 10]
                }
            },
            x: {
                ticks: {
                    color: '#000',
                    font: { size: 14, weight: 'bold' }
                },
                grid: {
                    display: false
                }
            }
        }
    }
});