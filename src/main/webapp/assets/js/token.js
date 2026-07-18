const inputs = document.querySelectorAll('.code-box');

inputs.forEach((input, index) => {
    // Detecta cuando el usuario escribe un número
    input.addEventListener('input', (e) => {
        if (e.target.value.length === 1 && index < inputs.length - 1) {
            inputs[index + 1].focus(); // Salta al siguiente input
        }
    });

    // Detecta cuando el usuario borra con "Backspace" para regresar
    input.addEventListener('keydown', (e) => {
        if (e.key === 'Backspace' && e.target.value.length === 0 && index > 0) {
            inputs[index - 1].focus(); // Regresa al anterior
        }
    });
});
const TOTAL_MINUTOS = 15;
const ID_CONTADOR = "contador";

function iniciarCronometro() {
    const contenedorContador = document.getElementById(ID_CONTADOR);
    if (!contenedorContador) return;

    let tiempoDestino = localStorage.getItem('cronometro_destino');
    const ahora = Math.floor(Date.now() / 1000);

    // Si no hay un tiempo de destino guardado o el tiempo ya expiró, creamos uno nuevo
    if (!tiempoDestino || parseInt(tiempoDestino) <= ahora) {
        tiempoDestino = ahora + (TOTAL_MINUTOS * 60);
        localStorage.setItem('cronometro_destino', tiempoDestino);
    }

    const intervalo = setInterval(() => {
        const tiempoActual = Math.floor(Date.now() / 1000);
        let segundosRestantes = parseInt(tiempoDestino) - tiempoActual;

        if (segundosRestantes <= 0) {
            clearInterval(intervalo);
            contenedorContador.textContent = "00:00";
            contenedorContador.style.color = "#8B0000"; // Color guinda al expirar
            localStorage.removeItem('cronometro_destino'); // Limpiar para el siguiente intento
            return;
        }

        let minutos = Math.floor(segundosRestantes / 60);
        let segundos = segundosRestantes % 60;

        // Formato con ceros a la izquierda (09:05)
        minutos = minutos < 10 ? '0' + minutos : minutos;
        segundos = segundos < 10 ? '0' + segundos : segundos;

        contenedorContador.textContent = `${minutos}:${segundos}`;
    }, 1000);
}

// 2. LÓGICA DE ENFOQUE AUTOMÁTICO PARA LOS 6 CASILLEROS DEL CÓDIGO
function configurarInputsCodigo() {
    const inputs = document.querySelectorAll('.code-box');
    inputs.forEach((input, index) => {
        input.addEventListener('input', () => {
            if (input.value.length === 1 && index < inputs.length - 1) {
                inputs[index + 1].focus();
            }
        });
        input.addEventListener('keydown', (e) => {
            if (e.key === 'Backspace' && input.value.length === 0 && index > 0) {
                inputs[index - 1].focus();
            }
        });
    });
}

// Inicializar funciones al cargar la página
document.addEventListener("DOMContentLoaded", () => {
    iniciarCronometro();
    configurarInputsCodigo();
});