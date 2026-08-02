const TOTAL_MINUTOS = 15;
const ID_CONTADOR = "contador";
let intervaloCronometro = null; // Guardamos la referencia para poder detenerlo al reenviar

function iniciarCronometro() {
    const contenedorContador = document.getElementById(ID_CONTADOR);
    if (!contenedorContador) return;

    // Si ya había un cronómetro corriendo, lo detenemos para evitar que se solapen dos intervalos
    if (intervaloCronometro) {
        clearInterval(intervaloCronometro);
    }

    let tiempoDestino = localStorage.getItem('cronometro_destino');
    const ahora = Math.floor(Date.now() / 1000);

    // Si no hay un tiempo guardado o ya expiró, calculamos los nuevos 15 minutos
    if (!tiempoDestino || parseInt(tiempoDestino) <= ahora) {
        tiempoDestino = ahora + (TOTAL_MINUTOS * 60);
        localStorage.setItem('cronometro_destino', tiempoDestino);
    }

    // Restablecemos el color por si estaba en guinda por expiración
    contenedorContador.style.color = "";

    intervaloCronometro = setInterval(() => {
        const tiempoActual = Math.floor(Date.now() / 1000);
        let segundosRestantes = parseInt(tiempoDestino) - tiempoActual;

        if (segundosRestantes <= 0) {
            clearInterval(intervaloCronometro);
            contenedorContador.textContent = "00:00";
            contenedorContador.style.color = "#8B0000"; // Color guinda al expirar
            localStorage.removeItem('cronometro_destino');
            return;
        }

        let minutos = Math.floor(segundosRestantes / 60);
        let segundos = segundosRestantes % 60;

        minutos = minutos < 10 ? '0' + minutos : minutos;
        segundos = segundos < 10 ? '0' + segundos : segundos;

        contenedorContador.textContent = `${minutos}:${segundos}`;
    }, 1000);
}

function reenviarCodigo() {
    const btn = document.getElementById('btnReenviar');

    // Deshabilitar el enlace temporalmente
    btn.style.pointerEvents = 'none';
    btn.style.opacity = '0.5';

    fetch('ReenviarCodigoServlet', {
        method: 'POST'
    })
        .then(response => {
            if (response.ok) {
                // 1. Borramos la marca de tiempo vieja para reiniciar a 15 mins
                localStorage.removeItem('cronometro_destino');

                // 2. Volvemos a arrancar el reloj desde cero
                iniciarCronometro();

                alert('Código reenviado con éxito a tu correo.');
            } else {
                alert('Hubo un error al reenviar el código.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Error de conexión al reenviar el código.');
        })
        .finally(() => {
            btn.style.pointerEvents = 'auto';
            btn.style.opacity = '1';
        });
}

// LÓGICA DE ENFOQUE AUTOMÁTICO PARA LOS CASILLEROS DEL CÓDIGO
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

// Inicializar al cargar la página
document.addEventListener("DOMContentLoaded", () => {
    iniciarCronometro();
    configurarInputsCodigo();
});
function filtrarPorCategoria(idCategoria) {
    document.getElementById('inputCategoriaId').value = idCategoria;
    document.getElementById('formFiltroCategoria').submit();
}