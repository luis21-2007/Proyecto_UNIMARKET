
let tiempoRestante = 15 * 60;
const contenedorContador = document.getElementById("contador" +
    "");

const cuentaRegresiva = setInterval(() => {
    // Calcular minutos y segundos restantes
    let minutos = Math.floor(tiempoRestante / 60);
    let segundos = tiempoRestante % 60;

    minutos = minutos < 10 ? '0' + minutos : minutos;
    segundos = segundos < 10 ? '0' + segundos : segundos;
    contenedorContador.textContent = `${minutos}:${segundos}`;

    // Si el contador llega a cero, detener la cuenta regresiva
    if (tiempoRestante <= 0) {
        clearInterval(cuentaRegresiva);
        contenedorContador.textContent = "00:00";
        contenedorContador.style.color = "#8B0000"; // Cambia a rojo guinda cuando expira

        // Opcional: Aquí puedes habilitar un botón o avisar al usuario que ya puede reenviar
    } else {
        tiempoRestante--; // Restar un segundo en cada ciclo
    }
}, 1000);